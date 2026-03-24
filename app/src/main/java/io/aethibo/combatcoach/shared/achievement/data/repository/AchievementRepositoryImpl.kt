package io.aethibo.combatcoach.shared.achievement.data.repository

import io.aethibo.combatcoach.shared.achievement.data.dao.AchievementDao
import io.aethibo.combatcoach.shared.achievement.data.exception.AchievementException
import io.aethibo.combatcoach.shared.achievement.domain.mapper.toDomain
import io.aethibo.combatcoach.shared.achievement.domain.mapper.toEntity
import io.aethibo.combatcoach.shared.achievement.domain.model.Achievement
import io.aethibo.combatcoach.shared.achievement.domain.repository.AchievementRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AchievementRepositoryImpl(
    private val dao: AchievementDao,
) : AchievementRepository {

    // ── Flows ────────────────────────────────────────────────────────────────
    // No explicit exception wrapping; Room handles Flow errors internally.
    override fun observeAll(): Flow<List<Achievement>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override fun observeEarned(): Flow<List<Achievement>> =
        dao.observeEarned().map { list -> list.map { it.toDomain() } }

    // ── One-shot Suspend Functions ────────────────────────────────────────────

    @Throws(AchievementException::class)
    override suspend fun getByKey(key: String): Achievement? =
        runCatchingPersistence {
            dao.getByKey(key)?.toDomain()
        }

    @Throws(AchievementException::class)
    override suspend fun updateProgress(key: String, progress: Int, earnedAt: Long?) =
        runCatchingPersistence {
            dao.updateProgress(key, progress, earnedAt)
        }

    @Throws(AchievementException::class)
    override suspend fun seedDefaults(achievements: List<Achievement>) =
        runCatchingPersistence {
            val entities = achievements.map { it.toEntity() }
            dao.insertAll(entities)
        }

    @Throws(AchievementException::class)
    override suspend fun countEarned(): Int =
        runCatchingPersistence {
            dao.countEarned()
        }

    // ── Persistence Helper ────────────────────────────────────────────────────

    /**
     * Standardizes error handling across the repository.
     * If the block throws an AchievementException, it passes through.
     * Otherwise, generic Exceptions are wrapped in AchievementException.PersistenceFailed.
     */
    private suspend fun <T> runCatchingPersistence(block: suspend () -> T): T =
        try {
            block()
        } catch (e: AchievementException) {
            throw e // Already typed, let it propagate
        } catch (e: Exception) {
            // Log the error here if you have a logger (e.g., Timber)
            throw AchievementException.PersistenceFailed(e)
        }
}
