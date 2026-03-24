package io.aethibo.combatcoach.features.settings

import io.aethibo.combatcoach.shared.user.domain.model.UserPrefs

enum class SettingsSheet { NONE, THEME, DISCIPLINE, WEIGHT_UNIT }

data class SettingsState(
    val prefs: UserPrefs = UserPrefs(),
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val activeSheet: SettingsSheet = SettingsSheet.NONE,
    val showResetDialog: Boolean = false,
    val appVersion: String = "1.0.0",
    val eventSink: (SettingsEvent) -> Unit = {},
)
