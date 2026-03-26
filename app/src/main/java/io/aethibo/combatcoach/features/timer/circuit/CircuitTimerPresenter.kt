package io.aethibo.combatcoach.features.timer.circuit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import io.aethibo.combatcoach.core.sound.TimerSoundCoordinator
import io.aethibo.combatcoach.core.ui.extensions.enumSaver
import io.aethibo.combatcoach.features.timer.utils.CountdownTimer
import io.aethibo.combatcoach.features.timer.utils.rememberTimerServiceController
import io.aethibo.combatcoach.shared.achievement.domain.usecase.CheckAndAwardAchievementsUseCase
import io.aethibo.combatcoach.shared.log.domain.model.WorkoutLog
import io.aethibo.combatcoach.shared.log.domain.usecase.SaveWorkoutLogUseCase
import io.aethibo.combatcoach.shared.timer.domain.model.TimerPhase
import io.aethibo.combatcoach.shared.timer.domain.model.TimerSessionState
import io.aethibo.combatcoach.shared.timer.domain.model.TimerSessionType
import io.aethibo.combatcoach.shared.timer.domain.repository.TimerRepository
import io.aethibo.combatcoach.shared.user.domain.model.UserPrefs
import io.aethibo.combatcoach.shared.user.domain.usecase.LoadUserPrefsUseCase
import io.aethibo.combatcoach.shared.workout.domain.model.Workout
import io.aethibo.combatcoach.shared.workout.domain.model.WorkoutType
import io.aethibo.combatcoach.shared.workout.domain.usecase.ObserveWorkoutByIdUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun circuitTimerPresenter(
    workoutId: Int,
    observeWorkoutById: ObserveWorkoutByIdUseCase,
    saveWorkoutLog: SaveWorkoutLogUseCase,
    checkAchievements: CheckAndAwardAchievementsUseCase,
    repository: TimerRepository,
    soundCoordinator: TimerSoundCoordinator,
    loadPrefs: LoadUserPrefsUseCase,
    onBack: () -> Unit,
    onWorkoutTypeResolved: ((WorkoutType) -> Unit)? = null,
): CircuitTimerState {

    val scope = rememberCoroutineScope()

    var workout by remember { mutableStateOf<Workout?>(null) }
    var currentRound by rememberSaveable { mutableIntStateOf(1) }
    var stationIndex by remember { mutableIntStateOf(0) }
    var phase by rememberSaveable(stateSaver = enumSaver()) { mutableStateOf(TimerPhase.IDLE) }
    var secondsLeft by rememberSaveable { mutableIntStateOf(0) }
    var totalElapsed by rememberSaveable { mutableIntStateOf(0) }
    var isPaused by rememberSaveable { mutableStateOf(false) }
    var showComplete by remember { mutableStateOf(false) }
    var showStopDialog by remember { mutableStateOf(false) }
    var showLeaveDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var prefs by remember { mutableStateOf(UserPrefs()) }

    val restTimer = remember {
        CountdownTimer(
            onTick = { remaining -> secondsLeft = remaining },
            onComplete = {
                val w = workout ?: return@CountdownTimer
                stationIndex = 0
                phase = TimerPhase.WORK
                secondsLeft = w.exercises.getOrNull(0)?.durationSeconds ?: 30
                soundCoordinator.onRoundStart(prefs)
            },
        )
    }

    val workTimer = remember {
        CountdownTimer(
            onTick = { remaining -> secondsLeft = remaining },
            onComplete = {
                val w = workout ?: return@CountdownTimer
                val nextStation = stationIndex + 1
                if (nextStation < w.exercises.size) {
                    stationIndex = nextStation
                    phase = TimerPhase.WORK
                    secondsLeft = w.exercises[nextStation].durationSeconds ?: 30
                    soundCoordinator.onRoundEnd(prefs)
                } else {
                    val nextRound = currentRound + 1
                    if (nextRound > w.circuitRounds) {
                        phase = TimerPhase.COMPLETE
                        showComplete = true
                        soundCoordinator.onRoundStart(prefs)
                    } else {
                        currentRound = nextRound
                        phase = TimerPhase.REST
                        secondsLeft = w.circuitRestBetweenRoundsSeconds
                        restTimer.start(scope, w.circuitRestBetweenRoundsSeconds)
                        soundCoordinator.onRoundEnd(prefs)
                    }
                }
            },
        )
    }

    rememberTimerServiceController(
        onPauseResumeRequested = {
            isPaused = !isPaused
            if (isPaused) {
                workTimer.cancel(); restTimer.cancel()
            } else when (phase) {
                TimerPhase.WORK -> workTimer.start(scope, secondsLeft)
                TimerPhase.REST -> restTimer.start(scope, secondsLeft)
                else -> {}
            }
        },
        onStopRequested = {
            isPaused = true
            workTimer.cancel(); restTimer.cancel()
            showStopDialog = true
        },
        onNextRequested = {
            advanceStation(
                workout, stationIndex, currentRound, scope, workTimer, restTimer,
                onStationChange = { stationIndex = it },
                onRoundChange = { currentRound = it },
                onPhaseChange = { phase = it },
                onSecondsChange = { secondsLeft = it },
                onComplete = { showComplete = true },
                soundCoordinator = soundCoordinator,
                prefs = prefs,
            )
        },
    )

    LaunchedEffect(Unit) {
        loadPrefs().fold(ifRight = { prefs = it }, ifLeft = {})
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1_000L)
            snapshotFlow { isPaused to phase }.first { (paused, ph) ->
                !paused && ph != TimerPhase.COMPLETE && ph != TimerPhase.IDLE
            }
            totalElapsed++
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { phase to stationIndex }
            .collect { (currentPhase, currentStation) ->
                if (currentPhase == TimerPhase.WORK) {
                    val duration =
                        workout?.exercises?.getOrNull(currentStation)?.durationSeconds ?: 30
                    workTimer.start(scope, duration)
                }
            }
    }

    LaunchedEffect(workoutId) {
        val w = observeWorkoutById(workoutId).filterNotNull().first()
        workout = w
        secondsLeft = w.exercises.getOrNull(0)?.durationSeconds ?: 30
        stationIndex = 0
        currentRound = 1
        phase = TimerPhase.WORK
        isLoading = false

        onWorkoutTypeResolved?.invoke(w.type)
        if (w.type != WorkoutType.CIRCUIT) return@LaunchedEffect

        repository.startSession(
            TimerSessionState.Active(
                sessionType = TimerSessionType.CIRCUIT,
                itemId = workoutId,
                sessionTitle = w.name,
                phase = TimerPhase.WORK,
                secondsRemaining = secondsLeft,
                currentPhaseTotalSeconds = secondsLeft,
                currentRound = 1,
                totalRounds = w.circuitRounds,
                currentExerciseName = w.exercises.getOrNull(0)?.name ?: "",
                totalExercises = w.exercises.size,
            )
        )
    }

    // Push secondsRemaining every tick for notification countdown + countdown beeps
    LaunchedEffect(secondsLeft) {
        if (phase == TimerPhase.IDLE || phase == TimerPhase.COMPLETE) return@LaunchedEffect
        repository.update {
            copy(
                secondsRemaining = secondsLeft,
                totalElapsedSeconds = totalElapsed
            )
        }
        soundCoordinator.onCountdownTick(secondsLeft, prefs)
    }

    LaunchedEffect(phase, currentRound, stationIndex, isPaused) {
        val w = workout ?: return@LaunchedEffect
        if (phase == TimerPhase.IDLE) return@LaunchedEffect
        val phaseTotalSeconds = when (phase) {
            TimerPhase.WORK -> w.exercises.getOrNull(stationIndex)?.durationSeconds ?: 30
            TimerPhase.REST -> w.circuitRestBetweenRoundsSeconds
            else -> 0
        }
        repository.update {
            copy(
                phase = phase,
                secondsRemaining = secondsLeft,
                currentPhaseTotalSeconds = phaseTotalSeconds,
                currentRound = currentRound,
                totalRounds = w.circuitRounds,
                currentExerciseIndex = stationIndex,
                currentExerciseName = w.exercises.getOrNull(stationIndex)?.name ?: "",
                sessionTitle = w.name,
                isPaused = isPaused,
                totalElapsedSeconds = totalElapsed,
            )
        }
    }

    fun discard() {
        workTimer.cancel(); restTimer.cancel()
        repository.clearSession()
        onBack()
    }

    fun saveAndFinish() {
        scope.launch {
            saveWorkoutLog(
                WorkoutLog(
                    id = 0,
                    workoutId = workoutId,
                    durationSeconds = totalElapsed
                )
            )
            checkAchievements()
            repository.clearSession()
            onBack()
        }
    }

    val eventSink: (CircuitTimerEvent) -> Unit = remember {
        { event ->
            when (event) {
                CircuitTimerEvent.PauseResume -> {
                    isPaused = !isPaused
                    if (isPaused) {
                        workTimer.cancel(); restTimer.cancel()
                    } else when (phase) {
                        TimerPhase.WORK -> workTimer.start(scope, secondsLeft)
                        TimerPhase.REST -> restTimer.start(scope, secondsLeft)
                        else -> {}
                    }
                }

                CircuitTimerEvent.NextStation -> advanceStation(
                    workout, stationIndex, currentRound, scope, workTimer, restTimer,
                    onStationChange = { stationIndex = it },
                    onRoundChange = { currentRound = it },
                    onPhaseChange = { phase = it },
                    onSecondsChange = { secondsLeft = it },
                    onComplete = { showComplete = true },
                    soundCoordinator = soundCoordinator,
                    prefs = prefs,
                )

                CircuitTimerEvent.SkipRest -> {
                    restTimer.cancel()
                    val w = workout ?: return@remember
                    stationIndex = 0
                    phase = TimerPhase.WORK
                    secondsLeft = w.exercises.getOrNull(0)?.durationSeconds ?: 30
                    soundCoordinator.onRoundStart(prefs)
                }

                is CircuitTimerEvent.AddTime -> {
                    val newRemaining = secondsLeft + event.seconds
                    secondsLeft = newRemaining
                    when (phase) {
                        TimerPhase.WORK -> workTimer.start(scope, newRemaining)
                        TimerPhase.REST -> restTimer.start(scope, newRemaining)
                        else -> {}
                    }
                }

                CircuitTimerEvent.RequestStop -> {
                    isPaused = true; workTimer.cancel(); restTimer.cancel(); showStopDialog = true
                }

                CircuitTimerEvent.DismissStop -> {
                    showStopDialog = false; isPaused = false
                    when (phase) {
                        TimerPhase.WORK -> workTimer.start(scope, secondsLeft)
                        TimerPhase.REST -> restTimer.start(scope, secondsLeft)
                        else -> {}
                    }
                }

                CircuitTimerEvent.ConfirmSaveStop -> {
                    showStopDialog = false; saveAndFinish()
                }

                CircuitTimerEvent.ConfirmDiscardStop -> {
                    showStopDialog = false; discard()
                }

                CircuitTimerEvent.Finish -> saveAndFinish()
                CircuitTimerEvent.DismissComplete -> {
                    showComplete = false; saveAndFinish()
                }

                CircuitTimerEvent.RequestLeave -> showLeaveDialog = true
                CircuitTimerEvent.KeepRunning -> showLeaveDialog = false
                CircuitTimerEvent.LeaveAndSave -> {
                    showLeaveDialog = false; saveAndFinish()
                }

                CircuitTimerEvent.LeaveAndDiscard -> {
                    showLeaveDialog = false; discard()
                }
            }
        }
    }

    return CircuitTimerState(
        workout = workout,
        currentRound = currentRound,
        currentStationIndex = stationIndex,
        phase = phase,
        secondsRemaining = secondsLeft,
        totalSecondsElapsed = totalElapsed,
        isPaused = isPaused,
        isLoading = isLoading,
        showComplete = showComplete,
        showStopDialog = showStopDialog,
        showLeaveDialog = showLeaveDialog,
        eventSink = eventSink,
    )
}

private fun advanceStation(
    workout: Workout?,
    stationIndex: Int,
    currentRound: Int,
    scope: CoroutineScope,
    workTimer: CountdownTimer,
    restTimer: CountdownTimer,
    onStationChange: (Int) -> Unit,
    onRoundChange: (Int) -> Unit,
    onPhaseChange: (TimerPhase) -> Unit,
    onSecondsChange: (Int) -> Unit,
    onComplete: () -> Unit,
    soundCoordinator: TimerSoundCoordinator,
    prefs: UserPrefs,
) {
    val w = workout ?: return
    workTimer.cancel(); restTimer.cancel()
    val nextStation = stationIndex + 1
    if (nextStation < w.exercises.size) {
        onStationChange(nextStation)
        onPhaseChange(TimerPhase.WORK)
        onSecondsChange(w.exercises[nextStation].durationSeconds ?: 30)
        soundCoordinator.onRoundStart(prefs)
    } else {
        val nextRound = currentRound + 1
        if (nextRound > w.circuitRounds) {
            onPhaseChange(TimerPhase.COMPLETE); onComplete()
        } else {
            onRoundChange(nextRound)
            onPhaseChange(TimerPhase.REST)
            onSecondsChange(w.circuitRestBetweenRoundsSeconds)
            restTimer.start(scope, w.circuitRestBetweenRoundsSeconds)
            soundCoordinator.onRoundStart(prefs)
        }
    }
}
