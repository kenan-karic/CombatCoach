package io.aethibo.combatcoach.features.timer.strength

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
import io.aethibo.combatcoach.shared.log.domain.model.ExerciseLog
import io.aethibo.combatcoach.shared.log.domain.model.WorkoutLog
import io.aethibo.combatcoach.shared.log.domain.usecase.ObserveLogsByWorkoutUseCase
import io.aethibo.combatcoach.shared.log.domain.usecase.SaveWorkoutLogUseCase
import io.aethibo.combatcoach.shared.timer.domain.model.PendingWeightEntry
import io.aethibo.combatcoach.shared.timer.domain.model.TimerPhase
import io.aethibo.combatcoach.shared.timer.domain.model.TimerSessionState
import io.aethibo.combatcoach.shared.timer.domain.model.TimerSessionType
import io.aethibo.combatcoach.shared.timer.domain.repository.TimerRepository
import io.aethibo.combatcoach.shared.user.domain.model.UserPrefs
import io.aethibo.combatcoach.shared.user.domain.usecase.LoadUserPrefsUseCase
import io.aethibo.combatcoach.shared.workout.domain.model.Workout
import io.aethibo.combatcoach.shared.workout.domain.model.WorkoutType
import io.aethibo.combatcoach.shared.workout.domain.usecase.ObserveWorkoutByIdUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun strengthTimerPresenter(
    workoutId: Int,
    observeWorkoutById: ObserveWorkoutByIdUseCase,
    saveWorkoutLog: SaveWorkoutLogUseCase,
    observeWorkoutLogs: ObserveLogsByWorkoutUseCase,
    checkAchievements: CheckAndAwardAchievementsUseCase,
    loadPrefs: LoadUserPrefsUseCase,
    repository: TimerRepository,
    soundCoordinator: TimerSoundCoordinator,
    onWorkoutTypeResolved: ((WorkoutType) -> Unit)? = null,
    onBack: () -> Unit,
): StrengthTimerState {

    val scope = rememberCoroutineScope()

    var workout by remember { mutableStateOf<Workout?>(null) }
    var exerciseIndex by remember { mutableIntStateOf(0) }
    var setIndex by remember { mutableIntStateOf(0) }
    var phase by rememberSaveable(stateSaver = enumSaver()) { mutableStateOf(TimerPhase.IDLE) }
    var restRemaining by remember { mutableIntStateOf(0) }
    var totalElapsed by rememberSaveable { mutableIntStateOf(0) }
    var isPaused by rememberSaveable { mutableStateOf(false) }
    var showComplete by remember { mutableStateOf(false) }
    var showStopDialog by remember { mutableStateOf(false) }
    var showLeaveDialog by remember { mutableStateOf(false) }
    var exerciseLogs by remember { mutableStateOf<List<ExerciseLog>>(emptyList()) }
    var previousWeights by remember { mutableStateOf<Map<Int, Float>>(emptyMap()) }
    var pendingWeightEntry by remember { mutableStateOf<PendingWeightEntry?>(null) }
    var pendingComplete by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var prefs by remember { mutableStateOf(UserPrefs()) }

    val restTimer = remember {
        CountdownTimer(
            onTick = { remaining -> restRemaining = remaining },
            onComplete = {
                phase = TimerPhase.WORK
                soundCoordinator.onRoundStart(prefs)
            },
        )
    }

    rememberTimerServiceController(
        onPauseResumeRequested = {
            isPaused = !isPaused
            if (isPaused) restTimer.cancel()
            else if (phase == TimerPhase.REST) restTimer.start(scope, restRemaining)
        },
        onStopRequested = {
            isPaused = true
            restTimer.cancel()
            showStopDialog = true
        },
        onNextRequested = {
            restTimer.cancel()
            phase = TimerPhase.WORK
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

    LaunchedEffect(workoutId) {
        val w = observeWorkoutById(workoutId).filterNotNull().first()
        workout = w
        phase = TimerPhase.WORK
        isLoading = false
        exerciseLogs = w.exercises.map { ex ->
            ExerciseLog(
                id = ex.id,
                exerciseName = ex.name,
                setsCompleted = 0,
                repsPerSet = emptyList(),
                weightPerSet = emptyList(),
            )
        }

        val allLogs = observeWorkoutLogs(workoutId.toString()).first()
        val lastSession = allLogs.firstOrNull()
        if (lastSession != null) {
            previousWeights = lastSession.exerciseLogs
                .mapNotNull { log ->
                    val lastWeight = log.weightPerSet.lastOrNull { it > 0f }
                    if (lastWeight != null) log.id to lastWeight else null
                }
                .toMap()
        }

        onWorkoutTypeResolved?.invoke(w.type)
        if (w.type != WorkoutType.STRENGTH) return@LaunchedEffect

        repository.startSession(
            TimerSessionState.Active(
                sessionType = TimerSessionType.STRENGTH,
                itemId = workoutId,
                sessionTitle = w.name,
                phase = TimerPhase.WORK,
                secondsRemaining = 0,
                currentPhaseTotalSeconds = 0,
                currentExerciseIndex = 0,
                currentExerciseName = w.exercises.getOrNull(0)?.name ?: "",
                totalExercises = w.exercises.size,
                totalSets = w.exercises.sumOf { it.sets },
            )
        )
    }

    // Push secondsRemaining every tick for notification countdown + countdown beeps
    LaunchedEffect(restRemaining) {
        if (phase != TimerPhase.REST) return@LaunchedEffect
        repository.update {
            copy(
                secondsRemaining = restRemaining,
                totalElapsedSeconds = totalElapsed
            )
        }
        soundCoordinator.onCountdownTick(restRemaining, prefs)
    }

    LaunchedEffect(phase, exerciseIndex, setIndex, isPaused) {
        val w = workout ?: return@LaunchedEffect
        if (phase == TimerPhase.IDLE) return@LaunchedEffect
        val restTotal = w.exercises.getOrNull(exerciseIndex)?.restSeconds ?: 0
        repository.update {
            copy(
                phase = phase,
                secondsRemaining = restRemaining,
                currentPhaseTotalSeconds = if (phase == TimerPhase.REST) restTotal else 0,
                currentExerciseIndex = exerciseIndex,
                currentExerciseName = w.exercises.getOrNull(exerciseIndex)?.name ?: "",
                currentSetIndex = setIndex,
                sessionTitle = w.name,
                isPaused = isPaused,
                totalElapsedSeconds = totalElapsed,
                exerciseLogs = exerciseLogs,
            )
        }
    }

    fun advanceToNextSet() {
        val currentExercise = workout?.exercises?.getOrNull(exerciseIndex) ?: return
        val newSetIndex = setIndex + 1
        if (newSetIndex >= currentExercise.sets) {
            val newExerciseIndex = exerciseIndex + 1
            if (newExerciseIndex >= (workout?.exercises?.size ?: 0)) {
                phase = TimerPhase.COMPLETE
                pendingComplete = true
                soundCoordinator.onRoundStart(prefs)
            } else {
                exerciseIndex = newExerciseIndex
                setIndex = 0
                val restSecs = currentExercise.restSeconds
                if (restSecs > 0) {
                    phase = TimerPhase.REST
                    restRemaining = restSecs
                    restTimer.start(scope, restSecs)
                }
                soundCoordinator.onRoundEnd(prefs)
            }
        } else {
            setIndex = newSetIndex
            val restSecs = currentExercise.restSeconds
            if (restSecs > 0) {
                phase = TimerPhase.REST
                restRemaining = restSecs
                restTimer.start(scope, restSecs)
            }
            soundCoordinator.onRoundEnd(prefs)
        }
    }

    fun recordSet(weightKg: Float? = null) {
        val ex = workout?.exercises?.getOrNull(exerciseIndex) ?: return
        exerciseLogs = exerciseLogs.mapIndexed { i, log ->
            if (i == exerciseIndex) log.copy(
                setsCompleted = log.setsCompleted + 1,
                repsPerSet = log.repsPerSet + (ex.reps ?: 0),
                weightPerSet = log.weightPerSet + (weightKg ?: 0f),
            ) else log
        }
    }

    fun discard() {
        restTimer.cancel()
        repository.clearSession()
        onBack()
    }

    fun saveAndFinish() {
        scope.launch {
            saveWorkoutLog(
                WorkoutLog(
                    id = 0,
                    workoutId = workoutId,
                    durationSeconds = totalElapsed,
                    exerciseLogs = exerciseLogs,
                )
            )
            checkAchievements()
            repository.clearSession()
            onBack()
        }
    }

    val eventSink: (StrengthTimerEvent) -> Unit = remember {
        { event ->
            when (event) {
                StrengthTimerEvent.SetComplete -> {
                    val ex = workout?.exercises?.getOrNull(exerciseIndex)
                    recordSet(weightKg = null)
                    advanceToNextSet()

                    if (ex != null && ex.reps != null) {
                        val prefill = exerciseLogs
                            .getOrNull(exerciseIndex)
                            ?.weightPerSet
                            ?.lastOrNull { it > 0f }
                            ?: previousWeights[ex.id]

                        pendingWeightEntry = PendingWeightEntry(
                            exerciseId = ex.id,
                            exerciseName = ex.name,
                            setIndex = setIndex,
                            repsCompleted = ex.reps,
                            previousWeightKg = prefill,
                        )
                    }
                }

                is StrengthTimerEvent.WeightConfirmed -> {
                    val entry = pendingWeightEntry ?: return@remember
                    val targetLogIndex =
                        exerciseLogs.indexOfFirst { it.id == entry.exerciseId }
                    if (targetLogIndex >= 0) {
                        val log = exerciseLogs[targetLogIndex]
                        val updatedWeights = log.weightPerSet.toMutableList()
                        if (updatedWeights.isNotEmpty()) updatedWeights[updatedWeights.lastIndex] =
                            event.weightKg
                        exerciseLogs = exerciseLogs.mapIndexed { i, l ->
                            if (i == targetLogIndex) l.copy(weightPerSet = updatedWeights) else l
                        }
                    }
                    pendingWeightEntry = null
                    if (pendingComplete) {
                        pendingComplete = false; showComplete = true
                    }
                }

                StrengthTimerEvent.WeightSkipped -> {
                    pendingWeightEntry = null
                    if (pendingComplete) {
                        pendingComplete = false; showComplete = true
                    }
                }

                StrengthTimerEvent.RequestStop -> {
                    isPaused = true; restTimer.cancel(); showStopDialog = true
                }

                StrengthTimerEvent.DismissStop -> {
                    showStopDialog = false; isPaused = false
                    if (phase == TimerPhase.REST) restTimer.start(scope, restRemaining)
                }

                StrengthTimerEvent.ConfirmSaveStop -> {
                    showStopDialog = false; saveAndFinish()
                }

                StrengthTimerEvent.ConfirmDiscardStop -> {
                    showStopDialog = false; discard()
                }

                StrengthTimerEvent.SkipRest -> {
                    restTimer.cancel(); phase = TimerPhase.WORK
                }

                StrengthTimerEvent.SkipExercise -> {
                    restTimer.cancel()
                    val next = exerciseIndex + 1
                    if (next >= (workout?.exercises?.size ?: 0)) {
                        phase = TimerPhase.COMPLETE; showComplete = true
                    } else {
                        exerciseIndex = next; setIndex = 0; phase = TimerPhase.WORK
                    }
                }

                is StrengthTimerEvent.AddRestTime -> {
                    val newRemaining = restRemaining + event.seconds
                    restRemaining = newRemaining
                    restTimer.start(scope, newRemaining)
                }

                StrengthTimerEvent.PauseResume -> {
                    isPaused = !isPaused
                    if (isPaused) restTimer.cancel()
                    else if (phase == TimerPhase.REST) restTimer.start(scope, restRemaining)
                }

                StrengthTimerEvent.Finish -> saveAndFinish()
                StrengthTimerEvent.DismissComplete -> {
                    showComplete = false; saveAndFinish()
                }

                StrengthTimerEvent.RequestLeave -> showLeaveDialog = true
                StrengthTimerEvent.KeepRunning -> showLeaveDialog = false
                StrengthTimerEvent.LeaveAndSave -> {
                    showLeaveDialog = false; saveAndFinish()
                }

                StrengthTimerEvent.LeaveAndDiscard -> {
                    showLeaveDialog = false; discard()
                }
            }
        }
    }

    return StrengthTimerState(
        workout = workout,
        currentExerciseIndex = exerciseIndex,
        currentSetIndex = setIndex,
        phase = phase,
        restSecondsRemaining = restRemaining,
        totalSecondsElapsed = totalElapsed,
        isLoading = isLoading,
        showComplete = showComplete,
        showStopDialog = showStopDialog,
        showLeaveDialog = showLeaveDialog,
        exerciseLogs = exerciseLogs,
        previousWeights = previousWeights,
        pendingWeightEntry = pendingWeightEntry,
        eventSink = eventSink,
    )
}
