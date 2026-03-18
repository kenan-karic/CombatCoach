package io.aethibo.combatcoach.features.dashboard.model

import io.aethibo.combatcoach.shared.combo.domain.model.Combo
import io.aethibo.combatcoach.shared.plan.domain.model.ActivePlan
import io.aethibo.combatcoach.shared.plan.domain.model.Plan
import io.aethibo.combatcoach.shared.workout.domain.model.Workout
import io.aethibo.combatcoach.shared.log.domain.model.DashboardStats

data class DashboardData(
    val workouts: List<Workout> = emptyList(),
    val combos: List<Combo> = emptyList(),
    val plans: List<Plan> = emptyList(),
    val activePlan: ActivePlan? = null,
    val stats: DashboardStats = DashboardStats(),
)
