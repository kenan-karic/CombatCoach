package io.aethibo.combatcoach.shared.log.data.repository

import io.aethibo.combatcoach.shared.log.data.dao.WorkoutLogDao
import io.aethibo.combatcoach.shared.log.data.exception.WorkoutLogException
import io.aethibo.combatcoach.shared.log.domain.repository.WorkoutLogRepository
import io.aethibo.combatcoach.shared.log.domain.mapper.toDomain
import io.aethibo.combatcoach.shared.log.domain.mapper.toEntity
import io.aethibo.combatcoach.shared.log.domain.model.WorkoutLog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.SerializationException

class WorkoutLogRepositoryImpl(
    private val dao: WorkoutLogDao,
) : WorkoutLogRepository {

    override fun observeAll(): Flow<List<WorkoutLog>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override fun observeByWorkout(workoutId: String): Flow<List<WorkoutLog>> =
        dao.observeByWorkout(workoutId).map { list -> list.map { it.toDomain() } }

    override fun observeByCombo(comboId: String): Flow<List<WorkoutLog>> =
        dao.observeByCombo(comboId).map { list -> list.map { it.toDomain() } }

    override fun observeByPlan(planId: String): Flow<List<WorkoutLog>> =
        dao.observeByPlan(planId).map { list -> list.map { it.toDomain() } }

    override fun observeSince(fromEpoch: Long): Flow<List<WorkoutLog>> =
        dao.observeSince(fromEpoch).map { list -> list.map { it.toDomain() } }

    override suspend fun getLastForWorkout(workoutId: String): WorkoutLog? =
        runCatchingPersistence { dao.getLastForWorkout(workoutId)?.toDomain() }

    override suspend fun getLastForCombo(comboId: String): WorkoutLog? =
        runCatchingPersistence { dao.getLastForCombo(comboId)?.toDomain() }

    override suspend fun save(log: WorkoutLog) {
        // Basic validation matching the Plan pattern
        if (log.workoutId == -1) throw WorkoutLogException.InvalidData("workoutId")

        val entity = try {
            log.toEntity()
        } catch (e: SerializationException) {
            throw WorkoutLogException.SerializationFailed(e)
        }

        runCatchingPersistence { dao.insert(entity) }
    }

    override suspend fun delete(id: Int) = runCatchingPersistence {
        val log = dao.getById(id) ?: throw WorkoutLogException.NotFound(id)
        dao.delete(log)
    }

    override suspend fun count(): Int =
        runCatchingPersistence { dao.count() }

    override suspend fun countThisWeek(weekStart: Long): Int =
        runCatchingPersistence { dao.countThisWeek(weekStart) }

    override suspend fun totalDurationSince(fromEpoch: Long): Int =
        runCatchingPersistence { dao.totalDurationSince(fromEpoch) ?: 0 }

    private suspend fun <T> runCatchingPersistence(block: suspend () -> T): T =
        try {
            block()
        } catch (e: WorkoutLogException) {
            throw e
        } catch (e: Exception) {
            throw WorkoutLogException.PersistenceFailed(e)
        }
}
