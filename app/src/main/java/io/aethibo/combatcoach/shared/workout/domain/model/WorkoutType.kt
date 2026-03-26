package io.aethibo.combatcoach.shared.workout.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Timer
import androidx.compose.ui.graphics.vector.ImageVector

enum class WorkoutType {
    /** Traditional gym session: sets × reps, rest timer between sets. */
    STRENGTH,

    /** Time-based circuit: each exercise runs on a countdown, auto or manual advance. */
    CIRCUIT;

    fun label(): String = when (this) {
        STRENGTH -> "Strength"
        CIRCUIT -> "Circuit"
    }

    fun icon(): ImageVector = when (this) {
        STRENGTH -> Icons.Filled.FitnessCenter
        CIRCUIT -> Icons.Filled.Timer
    }
}
