package io.aethibo.combatcoach.features.plandetail

import io.aethibo.combatcoach.shared.combo.domain.model.Combo
import io.aethibo.combatcoach.shared.plan.domain.model.ActivePlan
import io.aethibo.combatcoach.shared.plan.domain.model.Plan
import io.aethibo.combatcoach.shared.plan.domain.model.PlanDay
import io.aethibo.combatcoach.shared.workout.domain.model.Workout

data class WeekGroup(
    val weekNumber: Int,
    val label: String,
    val days: List<PlanDay>,
    val isExpanded: Boolean = false,
    val isCurrentWeek: Boolean = false,
    val completedCount: Int = 0,
)

data class PlanDetailState(
    val plan: Plan? = null,
    val activePlan: ActivePlan? = null,
    val workouts: Map<Int, Workout> = emptyMap(),
    val combos: Map<Int, Combo> = emptyMap(),
    val isActivePlan: Boolean = false,
    val isLoading: Boolean = true,
    val useWeekView: Boolean = false,
    val weekGroups: List<WeekGroup> = emptyList(),
    val expandedWeeks: Set<Int> = emptySet(),
    val showStartSheet: Boolean = false,
    val sheetDayIndex: Int = 0,
    val eventSink: (PlanDetailEvent) -> Unit = {},
) {
    val currentDayIndex: Int
        get() = (activePlan?.currentDay?.minus(1) ?: 0).coerceAtLeast(0)

    val currentDay: PlanDay?
        get() = plan?.days?.getOrNull(currentDayIndex)

    val progressFraction: Float
        get() {
            val total = plan?.totalDays?.toFloat() ?: return 0f
            return if (total == 0f) 0f
            else (activePlan?.currentDay?.minus(1) ?: 0).toFloat() / total
        }

    val progressPercent: Int
        get() = (progressFraction * 100).toInt()
}
