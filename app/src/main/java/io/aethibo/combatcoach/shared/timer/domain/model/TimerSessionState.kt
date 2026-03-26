package io.aethibo.combatcoach.shared.timer.domain.model

import io.aethibo.combatcoach.shared.log.domain.model.ExerciseLog

sealed interface TimerSessionState {

    data object Idle : TimerSessionState

    data class Active(
        // ── Identity ─────────────────────────────────────────────────────
        val sessionType: TimerSessionType,
        val itemId: Int,                        // workoutId or comboId
        val sessionTitle: String,               // shown in notification

        // ── Phase & timing ────────────────────────────────────────────────
        val phase: TimerPhase = TimerPhase.WORK,
        val secondsRemaining: Int = 0,
        val currentPhaseTotalSeconds: Int = 0,  // for progress calc
        val totalElapsedSeconds: Int = 0,
        val isPaused: Boolean = false,

        // ── Progress ──────────────────────────────────────────────────────
        // Strength / Circuit
        val currentExerciseIndex: Int = 0,
        val currentSetIndex: Int = 0,
        val totalExercises: Int = 0,
        val totalSets: Int = 0,
        val currentExerciseName: String = "",

        // Combat / Circuit
        val currentRound: Int = 1,
        val totalRounds: Int = 0,

        // ── Logged data (Strength) ────────────────────────────────────────
        val exerciseLogs: List<ExerciseLog> = emptyList(),
        val pendingWeightEntry: PendingWeightEntry? = null,

        // ── Dialog flags ──────────────────────────────────────────────────
        val showStopDialog: Boolean = false,
        val showLeaveDialog: Boolean = false,   // user tapped back/home mid-session
        val showComplete: Boolean = false,
        val pendingComplete: Boolean = false,
    ) : TimerSessionState {

        val progressFraction: Float
            get() = if (currentPhaseTotalSeconds == 0) 0f
            else (1f - secondsRemaining.toFloat() / currentPhaseTotalSeconds).coerceIn(0f, 1f)

        val overallProgressFraction: Float
            get() = when (sessionType) {
                TimerSessionType.STRENGTH -> {
                    val total = totalSets.toFloat()
                    if (total == 0f) 0f
                    else exerciseLogs.sumOf { it.setsCompleted }.toFloat() / total
                }

                TimerSessionType.COMBAT, TimerSessionType.CIRCUIT -> {
                    if (totalRounds == 0) 0f
                    else ((currentRound - 1).toFloat() / totalRounds).coerceIn(0f, 1f)
                }
            }

        val notificationText: String
            get() = when (phase) {
                TimerPhase.WORK -> when (sessionType) {
                    TimerSessionType.STRENGTH ->
                        "Set ${currentSetIndex + 1} · $currentExerciseName"

                    TimerSessionType.COMBAT, TimerSessionType.CIRCUIT ->
                        "Round $currentRound / $totalRounds"
                }

                TimerPhase.REST -> "Rest · ${secondsRemaining}s"
                TimerPhase.COMPLETE -> "Complete!"
                TimerPhase.IDLE -> ""
            }
    }
}
