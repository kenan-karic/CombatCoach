package io.aethibo.combatcoach.shared.achievement.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import io.aethibo.combatcoach.shared.achievement.data.dao.entity.AchievementEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AchievementDao {

    @Query("SELECT * FROM achievements ORDER BY category, earnedAt DESC")
    fun observeAll(): Flow<List<AchievementEntity>>

    @Query("SELECT * FROM achievements WHERE earnedAt IS NOT NULL ORDER BY earnedAt DESC")
    fun observeEarned(): Flow<List<AchievementEntity>>

    @Query("SELECT * FROM achievements WHERE key = :key")
    suspend fun getByKey(key: String): AchievementEntity?

    @Upsert
    suspend fun upsert(entity: AchievementEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(entities: List<AchievementEntity>)

    @Query("UPDATE achievements SET progressCurrent = :progress, earnedAt = :earnedAt WHERE key = :key")
    suspend fun updateProgress(key: String, progress: Int, earnedAt: Long?)

    @Query("SELECT COUNT(*) FROM achievements WHERE earnedAt IS NOT NULL")
    suspend fun countEarned(): Int
}
