package io.aethibo.combatcoach.shared.plan.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import io.aethibo.combatcoach.shared.plan.data.dao.entity.PlanEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlanDao {

    @Query("SELECT * FROM plans ORDER BY updatedAt DESC")
    fun observeAll(): Flow<List<PlanEntity>>

    @Query("SELECT * FROM plans WHERE id = :id")
    fun observeById(id: Int): Flow<PlanEntity?>

    @Query("SELECT * FROM plans WHERE id = :id")
    suspend fun getById(id: Int): PlanEntity?

    @Upsert
    suspend fun upsert(entity: PlanEntity)

    @Query("DELETE FROM plans WHERE id = :id")
    suspend fun deleteById(id: Int)
}
