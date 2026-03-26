package io.aethibo.combatcoach.shared.workout.domain.mapper

import io.aethibo.combatcoach.shared.workout.data.dao.entity.ExerciseEntity
import io.aethibo.combatcoach.shared.workout.domain.model.Exercise

fun ExerciseEntity.toDomain(): Exercise = Exercise(
    id = id,
    name = name,
    sets = sets,
    reps = reps,
    durationSeconds = durationSeconds,
    restSeconds = restSeconds,
    notes = notes,
    orderIndex = orderIndex,
)

fun Exercise.toEntity(): ExerciseEntity = ExerciseEntity(
    id = id,
    name = name,
    sets = sets,
    reps = reps,
    durationSeconds = durationSeconds,
    restSeconds = restSeconds,
    notes = notes,
    orderIndex = orderIndex,
)