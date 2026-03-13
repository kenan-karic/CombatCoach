package io.aethibo.combatcoach.features.shared.user.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import io.aethibo.combatcoach.features.shared.user.data.dao.entity.UserPrefsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserPrefsDao {

    @Query("SELECT value FROM user_prefs WHERE `key` = :key")
    suspend fun get(key: String): String?

    @Upsert
    suspend fun set(entity: UserPrefsEntity)

    @Query("DELETE FROM user_prefs WHERE `key` = :key")
    suspend fun delete(key: String)

    @Query("SELECT * FROM user_prefs")
    fun observeAll(): Flow<List<UserPrefsEntity>>
}
