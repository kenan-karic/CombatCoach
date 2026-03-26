package io.aethibo.combatcoach.shared.workout.domain.mapper

import io.aethibo.combatcoach.shared.utils.WorkoutDiscipline
import io.aethibo.combatcoach.shared.workout.data.dao.entity.ExerciseEntity
import io.aethibo.combatcoach.shared.workout.data.dao.entity.WorkoutEntity
import io.aethibo.combatcoach.shared.workout.domain.model.Workout
import io.aethibo.combatcoach.shared.workout.domain.model.WorkoutType
import kotlinx.serialization.json.Json

private val json = Json { ignoreUnknownKeys = true; coerceInputValues = true }

private fun String.toWorkoutDiscipline(): WorkoutDiscipline =
    try {
        WorkoutDiscipline.valueOf(this)
    } catch (e: IllegalArgumentException) {
        WorkoutDiscipline.STRENGTH
    }

private fun String.toWorkoutType(): WorkoutType =
    try {
        WorkoutType.valueOf(this)
    } catch (e: IllegalArgumentException) {
        WorkoutType.STRENGTH
    }

fun WorkoutEntity.toDomain(): Workout = Workout(
    id = id,
    name = name,
    description = description,
    workoutDiscipline = workoutDiscipline.toWorkoutDiscipline(),
    type = type.toWorkoutType(),
    circuitRounds = circuitRounds,
    circuitRestBetweenRoundsSeconds = circuitRestBetweenRoundsSeconds,
    exercises = json.decodeFromString<List<ExerciseEntity>>(exercisesJson).map { it.toDomain() },
    estimatedDurationMinutes = estimatedDurationMinutes,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun Workout.toEntity(): WorkoutEntity = WorkoutEntity(
    id = id,
    name = name,
    description = description,
    workoutDiscipline = workoutDiscipline.name,   // WorkoutDiscipline enum → String
    type = type.name,
    circuitRounds = circuitRounds,
    circuitRestBetweenRoundsSeconds = circuitRestBetweenRoundsSeconds,
    exercisesJson = json.encodeToString(exercises.map { it.toEntity() }),
    estimatedDurationMinutes = estimatedDurationMinutes,
    createdAt = createdAt,
    updatedAt = updatedAt,
)
