package io.aethibo.combatcoach.features.dashboard

import androidx.compose.runtime.Stable
import io.aethibo.combatcoach.shared.combo.domain.model.Combo
import io.aethibo.combatcoach.shared.plan.domain.utils.PlanProgress
import io.aethibo.combatcoach.shared.workout.domain.model.Workout
import io.aethibo.combatcoach.shared.log.domain.model.DashboardStats

@Stable
data class DashboardState(
    val isLoading: Boolean = true,
    val greeting: Int = -1,
    val today: String = "",
    val stats: DashboardStats = DashboardStats(),
    val activePlanProgress: PlanProgress? = null,
    val todayWorkouts: List<Workout> = emptyList(),
    val todayCombos: List<Combo> = emptyList(),
    val recentWorkouts: List<Workout> = emptyList(),
    val recentCombos: List<Combo> = emptyList(),
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
