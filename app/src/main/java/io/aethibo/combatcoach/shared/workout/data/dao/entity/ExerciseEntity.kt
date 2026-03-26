package io.aethibo.combatcoach.shared.workout.data.dao.entity

import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val sets: Int,
    val reps: Int?,                   // null for time-based
    val durationSeconds: Int?,        // null for rep-based
    val restSeconds: Int,
    val notes: String,
    val orderIndex: Int,
)