package io.aethibo.combatcoach.shared.workout.domain.model

import androidx.compose.runtime.Stable
import io.aethibo.combatcoach.shared.utils.WorkoutDiscipline

@Stable
data class Workout(
    val id: Int = 0,
    val name: String,
    val description: String = "",
    val workoutDiscipline: WorkoutDiscipline = WorkoutDiscipline.STRENGTH,
    val type: WorkoutType = WorkoutType.STRENGTH,
    val circuitRounds: Int = 3,
    val circuitRestBetweenRoundsSeconds: Int = 60,
    val exercises: List<Exercise> = emptyList(),
    val estimatedDurationMinutes: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
) {
    val isNew: Boolean get() = id == 0
    val exerciseCount: Int get() = exercises.size
    val totalSets: Int get() = exercises.sumOf { it.sets }

    fun withUpdatedAt(): Workout = copy(updatedAt = System.currentTimeMillis())
}
