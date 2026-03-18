package io.aethibo.combatcoach.shared.combo.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import io.aethibo.combatcoach.shared.combo.data.dao.entity.ComboEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ComboDao {

    @Query("SELECT * FROM combos ORDER BY updatedAt DESC")
    fun observeAll(): Flow<List<ComboEntity>>

    @Query("SELECT * FROM combos WHERE id = :id")
    fun observeById(id: Int): Flow<ComboEntity?>

    @Query("SELECT * FROM combos WHERE discipline = :discipline ORDER BY updatedAt DESC")
    fun observeByDiscipline(discipline: String): Flow<List<ComboEntity>>

    @Query("SELECT * FROM combos WHERE id = :id")
    suspend fun getById(id: Int): ComboEntity?

    @Upsert
    suspend fun upsert(entity: ComboEntity)

    @Delete
    suspend fun delete(entity: ComboEntity)

    @Query("DELETE FROM combos WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT COUNT(*) FROM combos")
    suspend fun count(): Int
}