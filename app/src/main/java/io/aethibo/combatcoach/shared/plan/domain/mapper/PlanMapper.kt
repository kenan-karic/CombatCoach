package io.aethibo.combatcoach.shared.plan.domain.mapper

import io.aethibo.combatcoach.shared.plan.data.dao.entity.PlanDayEntity
import io.aethibo.combatcoach.shared.plan.data.dao.entity.PlanEntity
import io.aethibo.combatcoach.shared.utils.Discipline
import io.aethibo.combatcoach.shared.plan.domain.model.Plan
import io.aethibo.combatcoach.shared.plan.domain.model.PlanDay
import io.aethibo.combatcoach.shared.plan.domain.model.PlanType
import kotlinx.serialization.json.Json

private val json = Json { ignoreUnknownKeys = true; coerceInputValues = true }

private fun String.toDisciplineModel(): Discipline =
    try {
        Discipline.valueOf(this)
    } catch (e: IllegalArgumentException) {
        Discipline.GENERAL
    }

fun PlanEntity.toDomain(): Plan = Plan(
    id = id,
    name = name,
    description = description,
    discipline = discipline.toDisciplineModel(),
    planType = PlanType.valueOf(planType),
    days = json.decodeFromString<List<PlanDayEntity>>(daysJson)
        .map { it.toDomain() },
    workoutIds = json.decodeFromString(workoutIdsJson),
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun Plan.toEntity(): PlanEntity = PlanEntity(
    id = id,
    name = name,
    description = description,
    discipline = discipline.name,
    planType = planType.name,
    daysJson = json.encodeToString(days.map { it.toEntity() }),
    workoutIdsJson = json.encodeToString(workoutIds),
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun PlanDayEntity.toDomain(): PlanDay = PlanDay(
    dayNumber = dayNumber,
    label = label,
    workoutIds = workoutIds,
    comboIds = comboIds,
    isRestDay = isRestDay,
)

fun PlanDay.toEntity(): PlanDayEntity = PlanDayEntity(
    dayNumber = dayNumber,
    label = label,
    workoutIds = workoutIds,
    comboIds = comboIds,
    isRestDay = isRestDay,
)