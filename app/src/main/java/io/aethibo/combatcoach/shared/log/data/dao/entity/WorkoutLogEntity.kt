package io.aethibo.combatcoach.shared.log.data.dao.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "workout_logs",
    indices = [
        Index(value = ["workoutId"]),
        Index(value = ["comboId"]),
        Index(value = ["planId"]),
        Index(value = ["completedAt"]),
    ]
)
data class WorkoutLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val workoutId: Int?,
    val comboId: Int?,
    val planId: Int?,
    val planDayNumber: Int?,
    val durationSeconds: Int,
    val completedAt: Long,
    val exerciseLogsJson: String,
    val notes: String,
    val rating: Int,
)
