package io.aethibo.combatcoachex.features.shared.log.domain.mapper

import io.aethibo.combatcoach.shared.log.data.dao.entity.ExerciseLogEntity
import io.aethibo.combatcoach.shared.log.data.dao.entity.WorkoutLogEntity
import io.aethibo.combatcoachex.features.shared.log.domain.model.WorkoutLog
import kotlinx.serialization.json.Json

private val json = Json { ignoreUnknownKeys = true; coerceInputValues = true }

fun WorkoutLogEntity.toDomain(): WorkoutLog = WorkoutLog(
    id = id,
    workoutId = workoutId,
    comboId = comboId,
    planId = planId,
    planDayNumber = planDayNumber,
    durationSeconds = durationSeconds,
    completedAt = completedAt,
    exerciseLogs = json.decodeFromString<List<ExerciseLogEntity>>(exerciseLogsJson)
        .map { exerciseEntity: ExerciseLogEntity -> exerciseEntity.toDomain() },
    notes = notes,
    rating = rating,
)

fun WorkoutLog.toEntity(): WorkoutLogEntity = WorkoutLogEntity(
    id = id,
    workoutId = workoutId,
    comboId = comboId,
    planId = planId,
    planDayNumber = planDayNumber,
    durationSeconds = durationSeconds,
    completedAt = completedAt,
    exerciseLogsJson = json.encodeToString(exerciseLogs.map { it.toEntity() }),
    notes = notes,
    rating = rating,
)