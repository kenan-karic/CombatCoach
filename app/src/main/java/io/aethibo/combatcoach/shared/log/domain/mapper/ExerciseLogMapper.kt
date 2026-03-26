package io.aethibo.combatcoach.shared.log.domain.mapper

import io.aethibo.combatcoach.shared.log.data.dao.entity.ExerciseLogEntity
import io.aethibo.combatcoach.shared.log.domain.model.ExerciseLog

fun ExerciseLogEntity.toDomain(): ExerciseLog = ExerciseLog(
    id = id,
    exerciseName = exerciseName,
    setsCompleted = setsCompleted,
    repsPerSet = repsPerSet,
    weightPerSet = weightPerSet,
    durationSeconds = durationSeconds,
    notes = notes,
)

fun ExerciseLog.toEntity(): ExerciseLogEntity = ExerciseLogEntity(
    id = id,
    exerciseName = exerciseName,
    setsCompleted = setsCompleted,
    repsPerSet = repsPerSet,
    weightPerSet = weightPerSet,
    durationSeconds = durationSeconds,
    notes = notes,
)
