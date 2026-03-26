package io.aethibo.combatcoach.shared.log.domain.repository

import io.aethibo.combatcoach.shared.log.data.exception.WorkoutLogException
import io.aethibo.combatcoach.shared.log.domain.model.WorkoutLog
import kotlinx.coroutines.flow.Flow

interface WorkoutLogRepository {
    fun observeAll(): Flow<List<WorkoutLog>>
    fun observeByWorkout(workoutId: String): Flow<List<WorkoutLog>>
    fun observeByCombo(comboId: String): Flow<List<WorkoutLog>>
    fun observeByPlan(planId: String): Flow<List<WorkoutLog>>
    fun observeSince(fromEpoch: Long): Flow<List<WorkoutLog>>

    @Throws(WorkoutLogException.PersistenceFailed::class)
    suspend fun getLastForWorkout(workoutId: String): WorkoutLog?

    @Throws(WorkoutLogException.PersistenceFailed::class)
    suspend fun getLastForCombo(comboId: String): WorkoutLog?

    @Throws(
        WorkoutLogException.InvalidData::class,
        WorkoutLogException.PersistenceFailed::class,
        WorkoutLogException.SerializationFailed::class
    )
    suspend fun save(log: WorkoutLog)

    @Throws(WorkoutLogException.NotFound::class, WorkoutLogException.PersistenceFailed::class)
    suspend fun delete(id: Int)

    @Throws(WorkoutLogException.PersistenceFailed::class)
    suspend fun count(): Int

    @Throws(WorkoutLogException.PersistenceFailed::class)
    suspend fun countThisWeek(weekStart: Long): Int

    @Throws(WorkoutLogException.PersistenceFailed::class)
    suspend fun totalDurationSince(fromEpoch: Long): Int
}