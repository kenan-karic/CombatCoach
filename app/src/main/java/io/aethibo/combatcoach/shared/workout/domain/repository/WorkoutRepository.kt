package io.aethibo.combatcoach.shared.workout.domain.repository

import io.aethibo.combatcoach.shared.workout.data.exception.WorkoutException
import io.aethibo.combatcoach.shared.workout.domain.model.Workout
import io.aethibo.combatcoach.shared.workout.domain.model.WorkoutType
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    fun observeAll(): Flow<List<Workout>>
    fun observeById(id: Int): Flow<Workout?>
    fun observeByType(type: WorkoutType): Flow<List<Workout>>

    @Throws(WorkoutException.PersistenceFailed::class)
    suspend fun getById(id: Int): Workout?

    @Throws(
        WorkoutException.InvalidData::class,
        WorkoutException.PersistenceFailed::class,
        WorkoutException.SerializationFailed::class,
    )
    suspend fun save(workout: Workout)

    @Throws(WorkoutException.PersistenceFailed::class)
    suspend fun delete(id: Int)

    suspend fun count(): Int
}
