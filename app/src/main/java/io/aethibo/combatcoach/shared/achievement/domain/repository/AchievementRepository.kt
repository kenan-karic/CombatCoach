package io.aethibo.combatcoach.shared.achievement.domain.repository

import io.aethibo.combatcoach.shared.achievement.data.exception.AchievementException
import io.aethibo.combatcoach.shared.achievement.domain.model.Achievement
import kotlinx.coroutines.flow.Flow

interface AchievementRepository {
    fun observeAll(): Flow<List<Achievement>>

    fun observeEarned(): Flow<List<Achievement>>

    @Throws(AchievementException::class)
    suspend fun getByKey(key: String): Achievement?

    @Throws(AchievementException::class)
    suspend fun updateProgress(key: String, progress: Int, earnedAt: Long?)

    @Throws(AchievementException::class)
    suspend fun seedDefaults(achievements: List<Achievement>)

    @Throws(AchievementException::class)
    suspend fun countEarned(): Int
}
