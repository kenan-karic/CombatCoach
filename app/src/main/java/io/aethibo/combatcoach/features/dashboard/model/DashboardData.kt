package io.aethibo.combatcoach.features.dashboard.model

data class DashboardData(
    val workouts: List<Workout> = emptyList(),
    val combos: List<Combo> = emptyList(),
    val plans: List<Plan> = emptyList(),
    val activePlan: ActivePlan? = null,
    val stats: DashboardStats = DashboardStats(),
)
