package io.aethibo.combatcoachex.features.shared.workout.data.repository

import io.aethibo.combatcoach.shared.workout.data.dao.WorkoutDao
import io.aethibo.combatcoach.shared.workout.data.exception.WorkoutException
import io.aethibo.combatcoach.shared.workout.domain.mapper.toDomain
import io.aethibo.combatcoach.shared.workout.domain.mapper.toEntity
import io.aethibo.combatcoach.shared.workout.domain.model.Workout
import io.aethibo.combatcoach.shared.workout.domain.model.WorkoutType
import io.aethibo.combatcoach.shared.workout.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.SerializationException

class WorkoutRepositoryImpl(
    private val dao: WorkoutDao,
) : WorkoutRepository {

    override fun observeAll(): Flow<List<Workout>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override fun observeById(id: Int): Flow<Workout?> =
        dao.observeById(id).map { it?.toDomain() }

    override fun observeByType(type: WorkoutType): Flow<List<Workout>> =
        dao.observeByType(type.name).map { list -> list.map { it.toDomain() } }

    override suspend fun getById(id: Int): Workout? =
        runCatchingPersistence { dao.getById(id)?.toDomain() }

    override suspend fun save(workout: Workout) {
        if (workout.name.isBlank()) throw WorkoutException.InvalidData("name")

        val entity = try {
            workout.toEntity()
        } catch (e: SerializationException) {
            throw WorkoutException.SerializationFailed(e)
        }

        runCatchingPersistence { dao.upsert(entity) }
    }

    override suspend fun delete(id: Int) =
        runCatchingPersistence { dao.deleteById(id) }

    override suspend fun count(): Int =
        runCatchingPersistence { dao.count() }

    private suspend fun <T> runCatchingPersistence(block: suspend () -> T): T =
        try {
            block()
        } catch (e: WorkoutException) {
            throw e
        } catch (e: Exception) {
            throw WorkoutException.PersistenceFailed(e)
        }
}
