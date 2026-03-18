package io.aethibo.combatcoach.features.create.combo

import io.aethibo.combatcoach.shared.combo.domain.model.AdvanceMode
import io.aethibo.combatcoach.shared.utils.Discipline

sealed interface ComboEditEvent {
    data class NameChanged(val value: String) : ComboEditEvent
    data class DescriptionChanged(val value: String) : ComboEditEvent
    data class DisciplineChanged(val value: Discipline) : ComboEditEvent
    data class AddStrike(val strike: String) : ComboEditEvent
    data class RemoveStrike(val index: Int) : ComboEditEvent
    data class ReorderStrikes(val from: Int, val to: Int) : ComboEditEvent
    data class DurationChanged(val seconds: Int) : ComboEditEvent
    data class RoundsChanged(val rounds: Int) : ComboEditEvent
    data class RestChanged(val seconds: Int) : ComboEditEvent
    data class AdvanceModeChanged(val mode: AdvanceMode) : ComboEditEvent
    data object Save : ComboEditEvent
    data object Delete : ComboEditEvent
    data object ConfirmDelete : ComboEditEvent
    data object DismissDelete : ComboEditEvent
    data object StartTimer : ComboEditEvent
}
