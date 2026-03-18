package io.aethibo.combatcoach.shared.plan.domain.model

data class ActivePlan(
    val planId: Int,
    val currentDay: Int = 1,
    val startedAt: Long = System.currentTimeMillis(),
    val lastActiveAt: Long = System.currentTimeMillis(),
)