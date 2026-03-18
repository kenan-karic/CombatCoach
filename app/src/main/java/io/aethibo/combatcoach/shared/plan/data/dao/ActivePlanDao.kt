package io.aethibo.combatcoach.shared.plan.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import io.aethibo.combatcoach.shared.plan.data.dao.entity.ActivePlanEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivePlanDao {

    @Query("SELECT * FROM active_plan WHERE id = 1")
    fun observe(): Flow<ActivePlanEntity?>

    @Query("SELECT * FROM active_plan WHERE id = 1")
    suspend fun get(): ActivePlanEntity?

    @Upsert
    suspend fun set(entity: ActivePlanEntity)

    @Query("DELETE FROM active_plan")
    suspend fun clear()
}
