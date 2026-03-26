package io.aethibo.combatcoach.features.timer.combat

sealed interface CombatTimerEvent {
    // ── Playback ──────────────────────────────────────────────────────────────
    data object PauseResume : CombatTimerEvent
    data object NextRound : CombatTimerEvent
    data object SkipRest : CombatTimerEvent
    data class AddTime(val seconds: Int) : CombatTimerEvent

    // ── Completion ────────────────────────────────────────────────────────────
    data object Finish : CombatTimerEvent
    data object DismissComplete : CombatTimerEvent

    // ── Stop (X button) ───────────────────────────────────────────────────────
    data object RequestStop : CombatTimerEvent
    data object ConfirmSaveStop : CombatTimerEvent
    data object ConfirmDiscardStop : CombatTimerEvent
    data object DismissStop : CombatTimerEvent

    // ── Leave (back / home) ───────────────────────────────────────────────────
    data object RequestLeave : CombatTimerEvent
    data object KeepRunning : CombatTimerEvent
    data object LeaveAndSave : CombatTimerEvent
    data object LeaveAndDiscard : CombatTimerEvent
}
