package io.aethibo.combatcoach.shared.combo.data.exception

sealed class ComboException(message: String? = null) : Exception(message) {

    /** No combo row exists for the requested id. */
    data class NotFound(val id: Int) : ComboException("Combo not found: id=$id")

    /** A required field (e.g. name) is blank or invalid. */
    data class InvalidData(val field: String) :
        ComboException("Invalid combo data — field '$field' failed validation")

    /** Room / SQLite write failed (constraint violation, disk full, etc.). */
    data class PersistenceFailed(override val cause: Throwable?) :
        ComboException("Failed to persist combo: ${cause?.message}")

    /** A JSON encode/decode step failed when (de)serialising strikes. */
    data class SerializationFailed(override val cause: Throwable?) :
        ComboException("Combo serialisation failed: ${cause?.message}")
}
