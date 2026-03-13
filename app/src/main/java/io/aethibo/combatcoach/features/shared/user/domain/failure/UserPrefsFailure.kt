package io.aethibo.combatcoach.features.shared.user.domain.failure

import io.aethibo.combatcoach.core.failure.Failure
import io.aethibo.combatcoach.features.shared.user.data.exceptions.UserPrefsException

sealed class UserPrefsFailure : Failure.FeatureFailure() {
    data object LoadFailed : UserPrefsFailure()
    data object SaveFailed : UserPrefsFailure()
    data class CorruptedValue(val key: String) : UserPrefsFailure()
}

fun UserPrefsException.mapToFailure(): UserPrefsFailure = when (this) {
    is UserPrefsException.LoadException -> UserPrefsFailure.LoadFailed
    is UserPrefsException.SaveException -> UserPrefsFailure.SaveFailed
    is UserPrefsException.CorruptedValueException -> UserPrefsFailure.CorruptedValue(key)
}
