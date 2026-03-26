package io.aethibo.combatcoach.features.timer.circuit

sealed interface CircuitTimerEvent {
    // ── Playback ──────────────────────────────────────────────────────────────
    data object PauseResume : CircuitTimerEvent
    data object NextStation : CircuitTimerEvent
    data object SkipRest : CircuitTimerEvent
    data class AddTime(val seconds: Int) : CircuitTimerEvent

    // ── Completion ────────────────────────────────────────────────────────────
    data object Finish : CircuitTimerEvent
    data object DismissComplete : CircuitTimerEvent

    // ── Stop (X button) ───────────────────────────────────────────────────────
    data object RequestStop : CircuitTimerEvent
    data object ConfirmSaveStop : CircuitTimerEvent
    data object ConfirmDiscardStop : CircuitTimerEvent
    data object DismissStop : CircuitTimerEvent

    // ── Leave (back / home) ───────────────────────────────────────────────────
    data object RequestLeave : CircuitTimerEvent
    data object KeepRunning : CircuitTimerEvent
    data object LeaveAndSave : CircuitTimerEvent
    data object LeaveAndDiscard : CircuitTimerEvent
}
