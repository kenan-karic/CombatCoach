package io.aethibo.combatcoach.features.timer.circuit

import io.aethibo.combatcoach.shared.timer.domain.model.TimerPhase
import io.aethibo.combatcoach.shared.workout.domain.model.Exercise
import io.aethibo.combatcoach.shared.workout.domain.model.Workout

data class CircuitTimerState(
    val workout: Workout? = null,
    val currentRound: Int = 1,
    val currentStationIndex: Int = 0,
    val phase: TimerPhase = TimerPhase.IDLE,
    val secondsRemaining: Int = 0,
    val totalSecondsElapsed: Int = 0,
    val isPaused: Boolean = false,
    val isLoading: Boolean = true,
    val showComplete: Boolean = false,
    val showStopDialog: Boolean = false,
    val showLeaveDialog: Boolean = false,
    val eventSink: (CircuitTimerEvent) -> Unit = {},
) {
    val totalRounds: Int get() = workout?.circuitRounds ?: 0
    val totalStations: Int get() = workout?.exercises?.size ?: 0
    val currentStation: Exercise? get() = workout?.exercises?.getOrNull(currentStationIndex)
    val isResting: Boolean get() = phase == TimerPhase.REST
    val isComplete: Boolean get() = phase == TimerPhase.COMPLETE

    val progressFraction: Float
        get() {
            val total = totalRounds
            return if (total == 0) 0f
            else ((currentRound - 1).toFloat() / total).coerceIn(0f, 1f)
        }

    val currentPhaseDuration: Int
        get() = when (phase) {
            TimerPhase.WORK -> currentStation?.durationSeconds ?: 0
            TimerPhase.REST -> workout?.circuitRestBetweenRoundsSeconds ?: 0
            else -> 0
        }
}
