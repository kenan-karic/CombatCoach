package io.aethibo.combatcoach.shared.plan.domain.utils

import androidx.compose.runtime.Stable
import io.aethibo.combatcoach.shared.plan.domain.model.ActivePlan
import io.aethibo.combatcoach.shared.plan.domain.model.Plan
import io.aethibo.combatcoach.shared.plan.domain.model.PlanType

@Stable
data class PlanProgress(
    val plan: Plan,
    val activePlan: ActivePlan,
    val completedDays: Int,
    val totalDays: Int,
) {
    val progressFraction: Float get() = completedDays.toFloat() / totalDays.toFloat()
    val isComplete: Boolean get() = completedDays >= totalDays
    val currentDayLabel: String
        get() = when (plan.planType) {
            PlanType.PROGRAM -> plan.days.getOrNull(activePlan.currentDay - 1)?.label
                ?: "Day ${activePlan.currentDay}"

            PlanType.COLLECTION -> "Workout ${activePlan.currentDay} of ${plan.workoutIds.size}"
        }
}