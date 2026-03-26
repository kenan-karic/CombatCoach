package io.aethibo.combatcoach.features.create.combo

import io.aethibo.combatcoach.core.failure.Failure
import io.aethibo.combatcoach.features.create.utils.CreateEditMode
import io.aethibo.combatcoach.shared.combo.domain.failure.ComboFailure
import io.aethibo.combatcoach.shared.combo.domain.model.AdvanceMode
import io.aethibo.combatcoach.shared.utils.Discipline

data class ComboEditState(
    val mode: CreateEditMode = CreateEditMode.Create,
    val name: String = "",
    val description: String = "",
    val discipline: Discipline = Discipline.STRIKING,
    val strikes: List<String> = emptyList(),
    val durationSeconds: Int = 180,
    val rounds: Int = 3,
    val restBetweenRoundsSeconds: Int = 60,
    val advanceMode: AdvanceMode = AdvanceMode.BOTH,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val nameError: String? = null,
    val errorMessage: String? = null,
    val showDeleteDialog: Boolean = false,
    val eventSink: (ComboEditEvent) -> Unit = {},
)

/**
 * Maps any [Failure] to a human-readable string for display in the UI.
 *
 * In a production app this should use string resources via a Context or a
 * UiError wrapper (see the error-handling article).  A plain String is used
 * here to keep the presenter free of Android dependencies.
 */
internal fun Failure.toUserMessage(): String = when (this) {
    is ComboFailure.NotFound -> "Combo #$id no longer exists."
    is ComboFailure.InvalidData -> "\"$field\" cannot be blank."
    is ComboFailure.PersistenceFailed -> "Could not save — please try again."
    is ComboFailure.SerializationFailed -> "Strike data is corrupted. Please recreate the combo."
    is Failure.Unknown -> "An unexpected error occurred."
    else -> "An unexpected error occurred."
}
