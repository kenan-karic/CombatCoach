package io.aethibo.combatcoach.features.plandetail.model

import io.aethibo.combatcoach.shared.plan.domain.model.PlanDay

data class WeekGroup(
    val weekNumber: Int,
    val label: String,
    val days: List<PlanDay>,
    val isExpanded: Boolean = false,
    val isCurrentWeek: Boolean = false,
    val completedCount: Int = 0,
)
