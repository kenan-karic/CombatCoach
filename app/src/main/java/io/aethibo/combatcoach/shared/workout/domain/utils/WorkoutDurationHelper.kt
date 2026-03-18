package io.aethibo.combatcoach.shared.workout.domain.utils

import io.aethibo.combatcoach.shared.workout.domain.model.Exercise

fun List<Exercise>.estimatedDurationMinutes(): Int {
    val totalSeconds = sumOf { ex ->
        val workSeconds = when {
            ex.durationSeconds != null -> ex.durationSeconds * ex.sets
            ex.reps != null -> ex.sets * (ex.reps * 3) // ~3s per rep estimate
            else -> 0
        }
        workSeconds + (ex.restSeconds * ex.sets)
    }
    return (totalSeconds / 60).coerceAtLeast(1)
}
