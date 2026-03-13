package io.aethibo.combatcoach.features.shared.user.data.exceptions

sealed class UserPrefsException(override val message: String? = null) : Exception(message) {
    class LoadException(message: String? = "Failed to load preferences") :
        UserPrefsException(message)

    class SaveException(message: String? = "Failed to save preferences") :
        UserPrefsException(message)

    class CorruptedValueException(val key: String) :
        UserPrefsException("Corrupted value for key: $key")
}
