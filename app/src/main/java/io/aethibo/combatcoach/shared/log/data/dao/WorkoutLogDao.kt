package io.aethibo.combatcoach.shared.log.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.aethibo.combatcoach.shared.log.data.dao.entity.WorkoutLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutLogDao {

    @Query("SELECT * FROM workout_logs ORDER BY completedAt DESC")
    fun observeAll(): Flow<List<WorkoutLogEntity>>

    @Query("SELECT * FROM workout_logs WHERE workoutId = :workoutId ORDER BY completedAt DESC")
    fun observeByWorkout(workoutId: String): Flow<List<WorkoutLogEntity>>

    @Query("SELECT * FROM workout_logs WHERE comboId = :comboId ORDER BY completedAt DESC")
    fun observeByCombo(comboId: String): Flow<List<WorkoutLogEntity>>

    @Query("SELECT * FROM workout_logs WHERE planId = :planId ORDER BY completedAt DESC")
    fun observeByPlan(planId: String): Flow<List<WorkoutLogEntity>>

    @Query(
        """
        SELECT * FROM workout_logs 
        WHERE completedAt >= :fromEpoch 
        ORDER BY completedAt DESC
    """
    )
    fun observeSince(fromEpoch: Long): Flow<List<WorkoutLogEntity>>

    @Query("SELECT * FROM workout_logs WHERE id = :id")
    suspend fun getById(id: Int): WorkoutLogEntity?

    @Query(
        """
        SELECT * FROM workout_logs 
        WHERE workoutId = :workoutId 
        ORDER BY completedAt DESC 
        LIMIT 1
    """
    )
    suspend fun getLastForWorkout(workoutId: String): WorkoutLogEntity?

    @Query(
        """
        SELECT * FROM workout_logs 
        WHERE comboId = :comboId 
        ORDER BY completedAt DESC 
        LIMIT 1
    """
    )
    suspend fun getLastForCombo(comboId: String): WorkoutLogEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: WorkoutLogEntity)

    @Delete
    suspend fun delete(entity: WorkoutLogEntity)

    @Query("SELECT COUNT(*) FROM workout_logs")
    suspend fun count(): Int

    @Query("SELECT COUNT(*) FROM workout_logs WHERE completedAt >= :weekStart")
    suspend fun countThisWeek(weekStart: Long): Int

    @Query(
        """
        SELECT SUM(durationSeconds) FROM workout_logs 
        WHERE completedAt >= :fromEpoch
    """
    )
    suspend fun totalDurationSince(fromEpoch: Long): Int?
}
