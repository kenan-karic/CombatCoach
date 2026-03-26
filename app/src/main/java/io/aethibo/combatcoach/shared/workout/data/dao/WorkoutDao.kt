package io.aethibo.combatcoach.shared.workout.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import io.aethibo.combatcoach.shared.workout.data.dao.entity.WorkoutEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {

    @Query("SELECT * FROM workouts ORDER BY updatedAt DESC")
    fun observeAll(): Flow<List<WorkoutEntity>>

    @Query("SELECT * FROM workouts WHERE id = :id")
    fun observeById(id: Int): Flow<WorkoutEntity?>

    @Query("SELECT * FROM workouts WHERE type = :type ORDER BY updatedAt DESC")
    fun observeByType(type: String): Flow<List<WorkoutEntity>>

    @Query("SELECT * FROM workouts WHERE workoutDiscipline = :workoutDiscipline ORDER BY updatedAt DESC")
    fun observeByDiscipline(workoutDiscipline: String): Flow<List<WorkoutEntity>>

    @Query("SELECT * FROM workouts WHERE id = :id")
    suspend fun getById(id: Int): WorkoutEntity?

    @Upsert
    suspend fun upsert(entity: WorkoutEntity)

    @Query("DELETE FROM workouts WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT COUNT(*) FROM workouts")
    suspend fun count(): Int
}
