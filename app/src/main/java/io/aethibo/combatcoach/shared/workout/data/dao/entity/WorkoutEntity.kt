package io.aethibo.combatcoach.shared.workout.data.dao.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workouts")
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val workoutDiscipline: String,
    val type: String,
    val exercisesJson: String,
    val estimatedDurationMinutes: Int,
    val circuitRounds: Int = 3,
    val circuitRestBetweenRoundsSeconds: Int = 60,
    val createdAt: Long,
    val updatedAt: Long,
)
