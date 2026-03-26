package io.aethibo.combatcoach.features.timer.combat

import io.aethibo.combatcoach.shared.combo.domain.model.Combo
import io.aethibo.combatcoach.shared.timer.domain.model.TimerPhase

data class CombatTimerState(
    val combo: Combo? = null,
    val currentRound: Int = 1,
    val phase: TimerPhase = TimerPhase.IDLE,
    val secondsRemaining: Int = 0,
    val totalSecondsElapsed: Int = 0,
    val isPaused: Boolean = false,
    val isLoading: Boolean = true,
    val showComplete: Boolean = false,
    val showStopDialog: Boolean = false,
    val showLeaveDialog: Boolean = false,
    val eventSink: (CombatTimerEvent) -> Unit = {},
) {
    val totalRounds: Int get() = combo?.rounds ?: 0
    val isResting: Boolean get() = phase == TimerPhase.REST
    val isComplete: Boolean get() = phase == TimerPhase.COMPLETE

    val progressFraction: Float
        get() {
            val combo = combo ?: return 0f
            val totalWork = combo.durationSeconds * combo.rounds
            val totalRest = combo.restBetweenRoundsSeconds * (combo.rounds - 1).coerceAtLeast(0)
            val total = (totalWork + totalRest).toFloat()
            return if (total == 0f) 0f
            else (totalSecondsElapsed.toFloat() / total).coerceIn(0f, 1f)
        }

    val currentPhaseDuration: Int
        get() = when (phase) {
            TimerPhase.WORK -> combo?.durationSeconds ?: 0
            TimerPhase.REST -> combo?.restBetweenRoundsSeconds ?: 0
            else -> 0
        }
}
