package io.aethibo.combatcoach.shared.plan.domain.repository

import io.aethibo.combatcoach.shared.plan.data.exception.PlanException
import io.aethibo.combatcoach.shared.plan.domain.model.ActivePlan
import io.aethibo.combatcoach.shared.plan.domain.model.Plan
import kotlinx.coroutines.flow.Flow

interface PlanRepository {
    fun observeAll(): Flow<List<Plan>>
    fun observeById(id: Int): Flow<Plan?>
    fun observeActivePlan(): Flow<ActivePlan?>

    @Throws(PlanException.PersistenceFailed::class)
    suspend fun getById(id: Int): Plan?

    @Throws(
        PlanException.InvalidData::class,
        PlanException.PersistenceFailed::class,
        PlanException.SerializationFailed::class,
    )
    suspend fun save(plan: Plan)

    @Throws(PlanException.PersistenceFailed::class)
    suspend fun delete(id: Int)

    @Throws(PlanException.PersistenceFailed::class)
    suspend fun setActivePlan(activePlan: ActivePlan)

    @Throws(PlanException.PersistenceFailed::class)
    suspend fun clearActivePlan()

    @Throws(PlanException.PersistenceFailed::class)
    suspend fun getActivePlan(): ActivePlan?

    /**
     * Increments [ActivePlan.currentDay] by 1.
     * @throws PlanException.NoActivePlan if no active plan is set.
     * @throws PlanException.PersistenceFailed on a DB write error.
     */
    @Throws(PlanException.NoActivePlan::class, PlanException.PersistenceFailed::class)
    suspend fun advanceActivePlanDay()
}
