package io.aethibo.combatcoach.features.dashboard

import androidx.compose.runtime.Stable
import kotlin.collections.isNotEmpty

@Stable
data class DashboardState(
    val greeting: Int = -1,
    val today: String = "",
    val stats: DashboardStats = DashboardStats(),
    val activePlanProgress: PlanProgress? = null,
    val todayWorkouts: List<Workout> = emptyList(),
    val todayCombos: List<Combo> = emptyList(),
    val recentWorkouts: List<Workout> = emptyList(),
    val recentCombos: List<Combo> = emptyList(),
    val isLoading: Boolean = true,
    val eventSink: (DashboardEvent) -> Unit = {},
) {
    val hasTodayContent: Boolean
        get() = todayWorkouts.isNotEmpty() || todayCombos.isNotEmpty()

    val hasRecentContent: Boolean
        get() = recentWorkouts.isNotEmpty() || recentCombos.isNotEmpty()

    val isEmpty: Boolean
        get() = !hasTodayContent && !hasRecentContent

    val todaySectionTitle: String
        get() = if (activePlanProgress != null) "Today's Session" else "Your Workouts"
}
