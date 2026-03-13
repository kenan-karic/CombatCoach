package io.aethibo.combatcoach.features.onboarding.error

import io.aethibo.combatcoach.R
import io.aethibo.combatcoach.core.failure.Failure
import io.aethibo.combatcoach.core.ui.error.UiError
import io.aethibo.combatcoach.features.shared.user.domain.failure.UserPrefsFailure

fun UserPrefsFailure.toUiError(): UiError = when (this) {
    is UserPrefsFailure.LoadFailed -> UiError(R.string.error_prefs_load_failed)
    is UserPrefsFailure.SaveFailed -> UiError(R.string.error_prefs_save_failed)
    is UserPrefsFailure.CorruptedValue -> UiError(R.string.error_prefs_corrupted, listOf(key))
}

fun Failure.toUiError(): UiError = when (this) {
    is UserPrefsFailure -> this.toUiError()
    else -> UiError(R.string.error_unknown)
}