package io.aethibo.combatcoach.features.plandetail.model

import io.aethibo.combatcoach.shared.combo.domain.model.Combo
import io.aethibo.combatcoach.shared.plan.domain.model.ActivePlan
import io.aethibo.combatcoach.shared.plan.domain.model.Plan
import io.aethibo.combatcoach.shared.workout.domain.model.Workout

data class PlanDetailData(
    val plan: Plan? = null,
    val workouts: List<Workout> = emptyList(),
    val combos: List<Combo> = emptyList(),
    val activePlan: ActivePlan? = null,
)
