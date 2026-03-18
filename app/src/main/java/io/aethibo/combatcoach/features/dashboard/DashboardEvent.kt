package io.aethibo.combatcoach.features.dashboard

sealed interface DashboardEvent {
    data object CreateWorkout : DashboardEvent
    data object CreateCombo : DashboardEvent
    data object CreatePlan : DashboardEvent
    data class OpenWorkout(val id: Int) : DashboardEvent
    data class OpenCombo(val id: Int) : DashboardEvent   // ← new
    data class OpenPlan(val id: Int) : DashboardEvent
    data class StartWorkout(val id: Int) : DashboardEvent
    data class StartCombo(val id: Int) : DashboardEvent
}
