package io.aethibo.combatcoach.features.timer.strength

sealed interface StrengthTimerEvent {
    // ── Playback ──────────────────────────────────────────────────────────────
    data object SetComplete : StrengthTimerEvent
    data object SkipRest : StrengthTimerEvent
    data object SkipExercise : StrengthTimerEvent
    data class AddRestTime(val seconds: Int) : StrengthTimerEvent
    data object PauseResume : StrengthTimerEvent
    data object Finish : StrengthTimerEvent
    data object DismissComplete : StrengthTimerEvent

    // ── Weight entry ──────────────────────────────────────────────────────────
    data class WeightConfirmed(val weightKg: Float) : StrengthTimerEvent
    data object WeightSkipped : StrengthTimerEvent

    // ── Stop (X button) ───────────────────────────────────────────────────────
    data object RequestStop : StrengthTimerEvent
    data object ConfirmSaveStop : StrengthTimerEvent
    data object ConfirmDiscardStop : StrengthTimerEvent
    data object DismissStop : StrengthTimerEvent

    // ── Leave (back / home) ───────────────────────────────────────────────────
    data object RequestLeave : StrengthTimerEvent
    data object KeepRunning : StrengthTimerEvent
    data object LeaveAndSave : StrengthTimerEvent
    data object LeaveAndDiscard : StrengthTimerEvent
}
