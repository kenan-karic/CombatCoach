package io.aethibo.combatcoach.shared.plan.domain.mapper

import io.aethibo.combatcoach.shared.plan.data.dao.entity.ActivePlanEntity
import io.aethibo.combatcoach.shared.plan.domain.model.ActivePlan

fun ActivePlanEntity.toDomain(): ActivePlan = ActivePlan(
    planId = planId,
    currentDay = currentDayNumber,
    startedAt = startedAt,
    lastActiveAt = lastActiveAt,
)

fun ActivePlan.toEntity(): ActivePlanEntity = ActivePlanEntity(
    id = 1,
    planId = planId,
    currentDayNumber = currentDay,
    startedAt = startedAt,
    lastActiveAt = lastActiveAt,
)
