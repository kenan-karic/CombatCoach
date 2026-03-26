package io.aethibo.combatcoach.features.create.utils

sealed interface CreateEditMode {
    data object Create : CreateEditMode

    /**
     * @param id The Room-generated primary key of the item being edited.
     *   Always > 0 for persisted records.
     */
    data class Edit(val id: Int) : CreateEditMode
}
