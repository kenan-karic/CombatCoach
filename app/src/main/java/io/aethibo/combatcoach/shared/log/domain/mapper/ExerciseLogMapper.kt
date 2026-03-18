package io.aethibo.combatcoachex.features.shared.log.domain.mapper

import io.aethibo.combatcoach.shared.log.data.dao.entity.ExerciseLogEntity
import io.aethibo.combatcoachex.features.shared.log.domain.model.ExerciseLog

fun ExerciseLogEntity.toDomain(): ExerciseLog = ExerciseLog(
    exerciseId = exerciseId,
    exerciseName = exerciseName,
    setsCompleted = setsCompleted,
    repsPerSet = repsPerSet,
    weightPerSet = weightPerSet,
    durationSeconds = durationSeconds,
    notes = notes,
)

fun ExerciseLog.toEntity(): ExerciseLogEntity = ExerciseLogEntity(
    exerciseId = exerciseId,
    exerciseName = exerciseName,
    setsCompleted = setsCompleted,
    repsPerSet = repsPerSet,
    weightPerSet = weightPerSet,
    durationSeconds = durationSeconds,
    notes = notes,
)
