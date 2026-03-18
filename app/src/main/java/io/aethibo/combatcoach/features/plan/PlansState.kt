package io.aethibo.combatcoach.features.plan

import io.aethibo.combatcoach.shared.plan.domain.model.ActivePlan
import io.aethibo.combatcoach.shared.plan.domain.model.Plan

data class PlansState(
    val allPlans: List<Plan> = emptyList(),
    val activePlan: ActivePlan? = null,
    val activePlanDetail: Plan? = null,
    val isLoading: Boolean = true,
    val eventSink: (PlansEvent) -> Unit = {},
) {
    val hasPlans: Boolean get() = allPlans.isNotEmpty()

    val sectionTitle: String
        get() = if (allPlans.isEmpty()) "No plans yet"
        else "All Plans (${allPlans.size})"
}
