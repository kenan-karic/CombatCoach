package io.aethibo.combatcoach.shared.combo.domain.usecase

import io.aethibo.combatcoach.shared.combo.domain.model.Combo
import kotlinx.coroutines.flow.Flow

/** Emits a single combo by id, or null when absent. */
fun interface ObserveComboByIdUseCase {
    operator fun invoke(id: Int): Flow<Combo?>
}