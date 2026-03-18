package io.aethibo.combatcoach.shared.combo.data.repository

import io.aethibo.combatcoach.shared.combo.data.dao.ComboDao
import io.aethibo.combatcoach.shared.combo.data.exception.ComboException
import io.aethibo.combatcoach.shared.combo.domain.mapper.toDomain
import io.aethibo.combatcoach.shared.combo.domain.mapper.toEntity
import io.aethibo.combatcoach.shared.combo.domain.model.Combo
import io.aethibo.combatcoach.shared.combo.domain.repository.ComboRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.SerializationException

/**
 * Room-backed implementation of [ComboRepository].
 *
 * This class is the ONLY place that knows about Room and SQLite.
 * It translates any raw exception into a typed [ComboException] so that
 * callers (use cases) never have to deal with SQLite or kotlinx.serialization
 * internals.
 */
class ComboRepositoryImpl(
    private val dao: ComboDao,
) : ComboRepository {

    // ── Flows (no exception wrapping needed — Room errors surface as Flow errors) ──

    override fun observeAll(): Flow<List<Combo>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override fun observeById(id: Int): Flow<Combo?> =
        dao.observeById(id).map { it?.toDomain() }

    // ── One-shot suspend functions ────────────────────────────────────────────

    override suspend fun getById(id: Int): Combo? =
        runCatchingPersistence { dao.getById(id)?.toDomain() }

    override suspend fun save(combo: Combo) {
        if (combo.name.isBlank()) throw ComboException.InvalidData("name")

        val entity = try {
            combo.toEntity()
        } catch (e: SerializationException) {
            throw ComboException.SerializationFailed(e)
        }

        runCatchingPersistence { dao.upsert(entity) }
    }

    override suspend fun delete(id: Int) =
        runCatchingPersistence { dao.deleteById(id) }

    override suspend fun count(): Int =
        runCatchingPersistence { dao.count() }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /**
     * Executes [block] and re-throws any [Exception] that is NOT already a
     * [ComboException] as [ComboException.PersistenceFailed].
     *
     * This keeps every call site clean while ensuring typed exceptions always
     * propagate upward.
     */
    private suspend fun <T> runCatchingPersistence(block: suspend () -> T): T =
        try {
            block()
        } catch (e: ComboException) {
            throw e  // already typed — pass through unchanged
        } catch (e: Exception) {
            throw ComboException.PersistenceFailed(e)
        }
}
