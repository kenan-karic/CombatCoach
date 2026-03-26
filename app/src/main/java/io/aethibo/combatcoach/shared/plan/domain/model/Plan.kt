package io.aethibo.combatcoach.shared.plan.domain.model

import io.aethibo.combatcoach.shared.utils.Discipline

data class Plan(
    val id: Int = 0,
    val name: String,
    val description: String = "",
    val discipline: Discipline = Discipline.GENERAL,
    val planType: PlanType = PlanType.PROGRAM,
    val days: List<PlanDay> = emptyList(),
    val workoutIds: List<Int> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
) {
    val isNew: Boolean get() = id == 0

    val totalDays: Int get() = when (planType) {
        PlanType.PROGRAM    -> days.size
        PlanType.COLLECTION -> workoutIds.size
    }

    fun withUpdatedAt(): Plan = copy(updatedAt = System.currentTimeMillis())
}
