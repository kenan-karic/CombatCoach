package io.aethibo.combatcoach.features.settings

import io.aethibo.combatcoach.shared.user.domain.model.ThemeMode
import io.aethibo.combatcoach.shared.user.domain.model.WeightUnit
import io.aethibo.combatcoach.shared.utils.Discipline

sealed interface SettingsEvent {
    data class ThemeModeChanged(val mode: ThemeMode) : SettingsEvent
    data class DefaultDisciplineChanged(val disc: Discipline) : SettingsEvent
    data class WeightUnitChanged(val unit: WeightUnit) : SettingsEvent
    data class SoundToggled(val enabled: Boolean) : SettingsEvent
    data class VibrationToggled(val enabled: Boolean) : SettingsEvent
    data class CountdownBeepsToggled(val enabled: Boolean) : SettingsEvent
    data object OpenThemePicker : SettingsEvent
    data object OpenDisciplinePicker : SettingsEvent
    data object OpenWeightUnitPicker : SettingsEvent
    data object DismissSheet : SettingsEvent
    data object RequestReset : SettingsEvent
    data object ConfirmReset : SettingsEvent
    data object DismissReset : SettingsEvent
}
