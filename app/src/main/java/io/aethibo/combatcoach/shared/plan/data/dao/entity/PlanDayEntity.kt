package io.aethibo.combatcoach.shared.plan.data.dao.entity

import kotlinx.serialization.Serializable

@Serializable
data class PlanDayEntity(
    val dayNumber: Int,
    val label: String,                // e.g. "Day 1 — Upper Body"
    val workoutIds: List<Int>,
    val comboIds: List<Int>,
    val isRestDay: Boolean,
)