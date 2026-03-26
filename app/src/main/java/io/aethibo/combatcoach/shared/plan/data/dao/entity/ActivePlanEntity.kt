package io.aethibo.combatcoach.shared.plan.data.dao.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "active_plan")
data class ActivePlanEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val planId: Int,
    val currentDayNumber: Int,
    val startedAt: Long,
    val lastActiveAt: Long,
)