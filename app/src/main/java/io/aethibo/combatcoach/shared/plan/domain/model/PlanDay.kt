package io.aethibo.combatcoach.shared.plan.domain.model

data class PlanDay(
    val dayNumber: Int,
    val label: String = "Day $dayNumber",
    val workoutIds: List<Int> = emptyList(),
    val comboIds: List<Int> = emptyList(),
    val isRestDay: Boolean = false,
) {
    val isEmpty: Boolean get() = workoutIds.isEmpty() && comboIds.isEmpty() && !isRestDay
}
