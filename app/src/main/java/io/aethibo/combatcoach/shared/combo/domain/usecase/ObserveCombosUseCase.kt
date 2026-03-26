package io.aethibo.combatcoach.shared.combo.domain.usecase

import io.aethibo.combatcoach.shared.combo.domain.model.Combo
import kotlinx.coroutines.flow.Flow

/** Emits all combos ordered by last-updated descending. */
fun interface ObserveCombosUseCase {
    operator fun invoke(): Flow<List<Combo>>
}

