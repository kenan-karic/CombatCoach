package io.aethibo.combatcoach.shared.plan.data.repository

import io.aethibo.combatcoach.shared.plan.data.dao.ActivePlanDao
import io.aethibo.combatcoach.shared.plan.data.dao.PlanDao
import io.aethibo.combatcoach.shared.plan.data.exception.PlanException
import io.aethibo.combatcoach.shared.plan.domain.mapper.toDomain
import io.aethibo.combatcoach.shared.plan.domain.mapper.toEntity
import io.aethibo.combatcoach.shared.plan.domain.model.ActivePlan
import io.aethibo.combatcoach.shared.plan.domain.model.Plan
import io.aethibo.combatcoach.shared.plan.domain.repository.PlanRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.SerializationException

class PlanRepositoryImpl(
    private val dao: PlanDao,
    private val activePlanDao: ActivePlanDao,
) : PlanRepository {

    override fun observeAll(): Flow<List<Plan>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override fun observeById(id: Int): Flow<Plan?> =
        dao.observeById(id).map { it?.toDomain() }

    override fun observeActivePlan(): Flow<ActivePlan?> =
        activePlanDao.observe().map { it?.toDomain() }

    override suspend fun getById(id: Int): Plan? =
        runCatchingPersistence { dao.getById(id)?.toDomain() }

    override suspend fun save(plan: Plan) {
        if (plan.name.isBlank()) throw PlanException.InvalidData("name")

        val entity = try {
            plan.toEntity()
        } catch (e: SerializationException) {
            throw PlanException.SerializationFailed(e)
        }

        runCatchingPersistence { dao.upsert(entity) }
    }

    override suspend fun delete(id: Int) =
        runCatchingPersistence { dao.deleteById(id) }

    override suspend fun setActivePlan(activePlan: ActivePlan) =
        runCatchingPersistence { activePlanDao.set(activePlan.toEntity()) }

    override suspend fun clearActivePlan() =
        runCatchingPersistence { activePlanDao.clear() }

    override suspend fun getActivePlan(): ActivePlan? =
        runCatchingPersistence { activePlanDao.get()?.toDomain() }

    override suspend fun advanceActivePlanDay() {
        // Throw typed exception instead of silently no-oping — callers need
        // to know there was nothing to advance so they can surface a message.
        val current = activePlanDao.get()
            ?: throw PlanException.NoActivePlan

        runCatchingPersistence {
            activePlanDao.set(
                current.copy(
                    currentDayNumber = current.currentDayNumber + 1,
                    lastActiveAt = System.currentTimeMillis(),
                )
            )
        }
    }

    private suspend fun <T> runCatchingPersistence(block: suspend () -> T): T =
        try {
            block()
        } catch (e: PlanException) {
            throw e
        } catch (e: Exception) {
            throw PlanException.PersistenceFailed(e)
        }
}
