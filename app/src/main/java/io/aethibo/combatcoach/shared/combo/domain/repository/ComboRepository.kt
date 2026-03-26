package io.aethibo.combatcoach.shared.combo.domain.repository

import io.aethibo.combatcoach.shared.combo.data.exception.ComboException
import io.aethibo.combatcoach.shared.combo.domain.model.Combo
import kotlinx.coroutines.flow.Flow

interface ComboRepository {

    /** Emits the full combo list ordered by last-updated descending. */
    fun observeAll(): Flow<List<Combo>>

    /** Emits a single combo by [id], or null when it does not exist. */
    fun observeById(id: Int): Flow<Combo?>

    /**
     * Returns the combo with [id], or null if absent.
     * Unlike [observeById] this is a one-shot suspend call, not a live stream.
     */
    @Throws(ComboException.PersistenceFailed::class)
    suspend fun getById(id: Int): Combo?

    /**
     * Inserts or updates [combo].
     *
     * When [combo.id] == 0 Room auto-generates the primary key (insert).
     * When [combo.id] > 0 Room updates the existing row (upsert).
     *
     * @throws ComboException.InvalidData if [combo.name] is blank.
     * @throws ComboException.PersistenceFailed on a DB write error.
     * @throws ComboException.SerializationFailed if the strike list cannot
     *   be encoded to JSON.
     */
    @Throws(
        ComboException.InvalidData::class,
        ComboException.PersistenceFailed::class,
        ComboException.SerializationFailed::class,
    )
    suspend fun save(combo: Combo)

    /**
     * Deletes the combo with [id].  No-ops silently if the id does not exist.
     *
     * @throws ComboException.PersistenceFailed on a DB write error.
     */
    @Throws(ComboException.PersistenceFailed::class)
    suspend fun delete(id: Int)

    /** Returns the total number of stored combos. */
    suspend fun count(): Int
}
