package io.aethibo.combatcoach.shared.workout.domain.model

import androidx.compose.runtime.Stable

@Stable
data class Exercise(
    val id: Int = 0,
    val name: String,
    val sets: Int = 3,
    val reps: Int? = 10,              // null for time-based exercises
    val durationSeconds: Int? = null, // null for rep-based exercises
    val restSeconds: Int = 60,
    val notes: String = "",
    val orderIndex: Int = 0,
) {
    val isTimeBased: Boolean get() = durationSeconds != null

    fun displayRepsOrDuration(): String = when {
        reps != null -> "$reps reps"
        durationSeconds != null -> "${durationSeconds}s"
        else -> "—"
    }
}

/**
 * Returns a copy of this exercise with fields pre-set for a CIRCUIT workout:
 * time-based by default (30s work), reps cleared.
 * Call this when adding a new exercise while the workout type is CIRCUIT.
 */
fun Exercise.asCircuitDefault(): Exercise = copy(
    reps = null,
    durationSeconds = durationSeconds ?: 30,
    restSeconds = restSeconds,
)

/**
 * Returns a copy pre-set for a STRENGTH workout:
 * rep-based by default, duration cleared.
 */
fun Exercise.asStrengthDefault(): Exercise = copy(
    reps = reps ?: 10,
    durationSeconds = null,
)
