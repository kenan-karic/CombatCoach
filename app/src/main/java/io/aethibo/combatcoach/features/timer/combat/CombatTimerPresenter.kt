package io.aethibo.combatcoach.features.timer.combat

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
import io.aethibo.combatcoach.shared.combo.domain.model.Combo
import io.aethibo.combatcoach.shared.combo.domain.usecase.ObserveComboByIdUseCase
import io.aethibo.combatcoach.shared.log.domain.model.WorkoutLog
import io.aethibo.combatcoach.shared.log.domain.usecase.SaveWorkoutLogUseCase
import io.aethibo.combatcoach.shared.timer.domain.model.TimerPhase
import io.aethibo.combatcoach.shared.timer.domain.model.TimerSessionState
import io.aethibo.combatcoach.shared.timer.domain.model.TimerSessionType
import io.aethibo.combatcoach.shared.timer.domain.repository.TimerRepository
import io.aethibo.combatcoach.shared.user.domain.model.UserPrefs
import io.aethibo.combatcoach.shared.user.domain.usecase.LoadUserPrefsUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun combatTimerPresenter(
    comboId: Int,
    observeComboById: ObserveComboByIdUseCase,
    saveWorkoutLog: SaveWorkoutLogUseCase,
    checkAchievements: CheckAndAwardAchievementsUseCase,
    loadPrefs: LoadUserPrefsUseCase,
    repository: TimerRepository,
    soundCoordinator: TimerSoundCoordinator,
    onBack: () -> Unit,
): CombatTimerState {

    val scope = rememberCoroutineScope()

    var combo by remember { mutableStateOf<Combo?>(null) }
    var currentRound by rememberSaveable { mutableIntStateOf(1) }
    var phase by rememberSaveable(stateSaver = enumSaver()) { mutableStateOf(TimerPhase.IDLE) }
    var secondsLeft by rememberSaveable { mutableIntStateOf(0) }
    var totalElapsed by rememberSaveable { mutableIntStateOf(0) }
    var isPaused by rememberSaveable { mutableStateOf(false) }
    var showComplete by remember { mutableStateOf(false) }
    var showStopDialog by remember { mutableStateOf(false) }
    var showLeaveDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var prefs by remember { mutableStateOf(UserPrefs()) }

    val workTimer = remember {
        CountdownTimer(
            onTick = { remaining -> secondsLeft = remaining },
            onComplete = {
                val c = combo ?: return@CountdownTimer
                if (currentRound >= c.rounds) {
                    phase = TimerPhase.COMPLETE
                    showComplete = true
                    soundCoordinator.onRoundStart(prefs)
                } else {
                    phase = TimerPhase.REST
                    secondsLeft = c.restBetweenRoundsSeconds
                    soundCoordinator.onRoundEnd(prefs)
                }
            },
        )
    }

    val restTimer = remember {
        CountdownTimer(
            onTick = { remaining -> secondsLeft = remaining },
            onComplete = {
                currentRound++
                val c = combo ?: return@CountdownTimer
                phase = TimerPhase.WORK
                secondsLeft = c.durationSeconds
                workTimer.start(scope, c.durationSeconds)
                soundCoordinator.onRoundStart(prefs)
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
            restTimer.cancel()
            val c = combo ?: return@rememberTimerServiceController
            val nextRound = currentRound + 1
            if (nextRound > c.rounds) {
                // Last round just ended — go to COMPLETE
                phase = TimerPhase.COMPLETE
                showComplete = true
                soundCoordinator.onRoundStart(prefs)
            } else {
                currentRound = nextRound
                phase = TimerPhase.WORK
                secondsLeft = c.durationSeconds
                workTimer.start(scope, c.durationSeconds)
                soundCoordinator.onRoundStart(prefs)
            }
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
        snapshotFlow { phase }
            .collect { currentPhase ->
                if (currentPhase == TimerPhase.REST) {
                    val c = combo ?: return@collect
                    restTimer.start(scope, c.restBetweenRoundsSeconds)
                }
            }
    }

    LaunchedEffect(comboId) {
        val c = observeComboById(comboId).filterNotNull().first()
        combo = c
        isLoading = false
        secondsLeft = c.durationSeconds
        phase = TimerPhase.WORK
        repository.startSession(
            TimerSessionState.Active(
                sessionType = TimerSessionType.COMBAT,
                itemId = comboId,
                sessionTitle = c.name,
                phase = TimerPhase.WORK,
                secondsRemaining = c.durationSeconds,
                currentPhaseTotalSeconds = c.durationSeconds,
                currentRound = 1,
                totalRounds = c.rounds,
            )
        )
        workTimer.start(scope, c.durationSeconds)
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

    LaunchedEffect(phase, currentRound, isPaused) {
        val c = combo ?: return@LaunchedEffect
        if (phase == TimerPhase.IDLE) return@LaunchedEffect
        val phaseTotalSeconds = when (phase) {
            TimerPhase.WORK -> c.durationSeconds
            TimerPhase.REST -> c.restBetweenRoundsSeconds
            else -> 0
        }
        repository.update {
            copy(
                phase = phase,
                secondsRemaining = secondsLeft,
                currentPhaseTotalSeconds = phaseTotalSeconds,
                currentRound = currentRound,
                totalRounds = c.rounds,
                sessionTitle = c.name,
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

    fun advanceRound() {
        val c = combo ?: return
        restTimer.cancel(); workTimer.cancel()
        if (currentRound >= c.rounds) {
            phase = TimerPhase.COMPLETE
            showComplete = true
        } else {
            currentRound++
            phase = TimerPhase.WORK
            secondsLeft = c.durationSeconds
            workTimer.start(scope, c.durationSeconds)
            soundCoordinator.onRoundStart(prefs)
        }
    }

    fun saveAndFinish() {
        scope.launch {
            saveWorkoutLog(WorkoutLog(id = 0, comboId = comboId, durationSeconds = totalElapsed))
            checkAchievements()
            repository.clearSession()
            onBack()
        }
    }

    val eventSink: (CombatTimerEvent) -> Unit = remember {
        { event ->
            when (event) {
                CombatTimerEvent.PauseResume -> {
                    isPaused = !isPaused
                    if (isPaused) {
                        workTimer.cancel(); restTimer.cancel()
                    } else when (phase) {
                        TimerPhase.WORK -> workTimer.start(scope, secondsLeft)
                        TimerPhase.REST -> restTimer.start(scope, secondsLeft)
                        else -> {}
                    }
                }

                CombatTimerEvent.NextRound -> advanceRound()

                CombatTimerEvent.SkipRest -> {
                    restTimer.cancel()
                    val c = combo ?: return@remember
                    val nextRound = currentRound + 1
                    if (nextRound > c.rounds) {
                        // Last round just ended — go to COMPLETE
                        phase = TimerPhase.COMPLETE
                        showComplete = true
                        soundCoordinator.onRoundStart(prefs)
                    } else {
                        currentRound = nextRound
                        phase = TimerPhase.WORK
                        secondsLeft = c.durationSeconds
                        workTimer.start(scope, c.durationSeconds)
                        soundCoordinator.onRoundStart(prefs)
                    }
                }

                is CombatTimerEvent.AddTime -> {
                    val newRemaining = secondsLeft + event.seconds
                    secondsLeft = newRemaining
                    when (phase) {
                        TimerPhase.WORK -> workTimer.start(scope, newRemaining)
                        TimerPhase.REST -> restTimer.start(scope, newRemaining)
                        else -> {}
                    }
                }

                CombatTimerEvent.RequestStop -> {
                    isPaused = true
                    workTimer.cancel(); restTimer.cancel()
                    showStopDialog = true
                }

                CombatTimerEvent.DismissStop -> {
                    showStopDialog = false
                    isPaused = false
                    when (phase) {
                        TimerPhase.WORK -> workTimer.start(scope, secondsLeft)
                        TimerPhase.REST -> restTimer.start(scope, secondsLeft)
                        else -> {}
                    }
                }

                CombatTimerEvent.ConfirmSaveStop -> {
                    showStopDialog = false; saveAndFinish()
                }

                CombatTimerEvent.ConfirmDiscardStop -> {
                    showStopDialog = false; discard()
                }

                CombatTimerEvent.Finish -> saveAndFinish()
                CombatTimerEvent.DismissComplete -> {
                    showComplete = false; saveAndFinish()
                }

                CombatTimerEvent.RequestLeave -> showLeaveDialog = true
                CombatTimerEvent.KeepRunning -> showLeaveDialog = false
                CombatTimerEvent.LeaveAndSave -> {
                    showLeaveDialog = false; saveAndFinish()
                }

                CombatTimerEvent.LeaveAndDiscard -> {
                    showLeaveDialog = false; discard()
                }
            }
        }
    }

    return CombatTimerState(
        combo = combo,
        currentRound = currentRound,
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
