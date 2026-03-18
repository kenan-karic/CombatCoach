package io.aethibo.combatcoach.shared.plan.data.dao.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plans")
data class PlanEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val discipline: String,
    val planType: String,             // PlanType enum: PROGRAM | COLLECTION
    val daysJson: String,             // JSON-encoded List<PlanDayEntity> (for PROGRAM)
    val workoutIdsJson: String,       // JSON-encoded List<String> (for COLLECTION)
    val createdAt: Long,
    val updatedAt: Long,
)