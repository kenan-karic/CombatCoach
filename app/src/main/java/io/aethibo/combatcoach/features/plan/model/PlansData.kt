package io.aethibo.combatcoach.features.plan.model

import io.aethibo.combatcoach.shared.plan.domain.model.ActivePlan
import io.aethibo.combatcoach.shared.plan.domain.model.Plan

data class PlansData(
    val plans: List<Plan> = emptyList(),
    val activePlan: ActivePlan? = null,
)
