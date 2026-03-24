package io.aethibo.combatcoach.features.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.SportsMartialArts
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.Vibration
import androidx.compose.material.icons.outlined.VolumeUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing
import io.aethibo.combatcoachex.core.ui.theme.LocalSpacing
import io.aethibo.combatcoachex.features.settings.presentation.components.ActionRow
import io.aethibo.combatcoachex.features.settings.presentation.components.AppBrandingFooter
import io.aethibo.combatcoachex.features.settings.presentation.components.AppInfoRow
import io.aethibo.combatcoachex.features.settings.presentation.components.DisciplinePickerSheet
import io.aethibo.combatcoachex.features.settings.presentation.components.PickerRow
import io.aethibo.combatcoachex.features.settings.presentation.components.ResetConfirmDialog
import io.aethibo.combatcoachex.features.settings.presentation.components.SettingsDivider
import io.aethibo.combatcoachex.features.settings.presentation.components.SettingsHeader
import io.aethibo.combatcoachex.features.settings.presentation.components.SettingsSection
import io.aethibo.combatcoachex.features.settings.presentation.components.ThemePickerSheet
import io.aethibo.combatcoachex.features.settings.presentation.components.ToggleRow
import io.aethibo.combatcoachex.features.settings.presentation.components.WeightUnitPickerSheet

@Composable
fun SettingsScreen(state: SettingsState) {
    val sp = LocalSpacing.current

    if (state.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
    ) {

        // ── Header ─────────────────────────────────────────
        SettingsHeader(isSaving = state.isSaving)

        // ── Appearance ─────────────────────────────────────
        SettingsSection(title = "Appearance") {
            PickerRow(
                icon = Icons.Outlined.Palette,
                label = "Theme",
                value = state.prefs.themeMode.label(),
                onClick = { state.eventSink(SettingsEvent.OpenThemePicker) },
            )
        }

        // ── Training defaults ──────────────────────────────
        SettingsSection(title = "Training") {
            PickerRow(
                icon = Icons.Outlined.SportsMartialArts,
                label = "Default discipline",
                value = state.prefs.defaultDiscipline.label(),
                onClick = { state.eventSink(SettingsEvent.OpenDisciplinePicker) },
            )
            SettingsDivider()
            PickerRow(
                icon = Icons.Outlined.FitnessCenter,
                label = "Weight unit",
                value = state.prefs.weightUnit.label(),
                onClick = { state.eventSink(SettingsEvent.OpenWeightUnitPicker) },
            )
        }

        // ── Audio & Haptics ────────────────────────────────
        SettingsSection(title = "Audio & Haptics") {
            ToggleRow(
                icon = Icons.Outlined.VolumeUp,
                label = "Sound effects",
                subLabel = "Timer beeps and completion chimes",
                checked = state.prefs.soundEnabled,
                onToggle = { state.eventSink(SettingsEvent.SoundToggled(it)) },
            )
            SettingsDivider()
            ToggleRow(
                icon = Icons.Outlined.Vibration,
                label = "Haptic feedback",
                subLabel = "Vibration on set complete and round change",
                checked = state.prefs.vibrationEnabled,
                onToggle = { state.eventSink(SettingsEvent.VibrationToggled(it)) },
            )
            SettingsDivider()
            ToggleRow(
                icon = Icons.Outlined.Timer,
                label = "Countdown beeps",
                subLabel = "Beep at 3–2–1 before round ends",
                checked = state.prefs.countdownBeeps,
                onToggle = { state.eventSink(SettingsEvent.CountdownBeepsToggled(it)) },
            )
        }

        // ── Data ───────────────────────────────────────────
        SettingsSection(title = "Data") {
            ActionRow(
                icon = Icons.Outlined.RestartAlt,
                label = "Reset preferences",
                subLabel = "Restore all settings to defaults",
                color = MaterialTheme.colorScheme.error,
                onClick = { state.eventSink(SettingsEvent.RequestReset) },
            )
        }

        // ── About ──────────────────────────────────────────
        SettingsSection(title = "About") {
            AppInfoRow(
                icon = Icons.Outlined.Info,
                label = "Version",
                value = state.appVersion,
            )
            SettingsDivider()
            AppInfoRow(
                icon = Icons.Outlined.Shield,
                label = "Data storage",
                value = "Local only",
            )
            SettingsDivider()
            AppInfoRow(
                icon = Icons.Outlined.CloudOff,
                label = "Network",
                value = "Offline only",
            )
            SettingsDivider()
            AppInfoRow(
                icon = Icons.Outlined.Lock,
                label = "Account required",
                value = "None",
            )
        }

        // ── App branding footer ────────────────────────────
        AppBrandingFooter()

        Spacer(height(sp.xxl))
    }

    // ── Sheets ────────────────────────────────────────────
    when (state.activeSheet) {
        SettingsSheet.THEME -> ThemePickerSheet(
            current = state.prefs.themeMode,
            onSelect = { state.eventSink(SettingsEvent.ThemeModeChanged(it)) },
            onDismiss = { state.eventSink(SettingsEvent.DismissSheet) },
        )

        SettingsSheet.DISCIPLINE -> DisciplinePickerSheet(
            current = state.prefs.defaultDiscipline,
            onSelect = { state.eventSink(SettingsEvent.DefaultDisciplineChanged(it)) },
            onDismiss = { state.eventSink(SettingsEvent.DismissSheet) },
        )

        SettingsSheet.WEIGHT_UNIT -> WeightUnitPickerSheet(
            current = state.prefs.weightUnit,
            onSelect = { state.eventSink(SettingsEvent.WeightUnitChanged(it)) },
            onDismiss = { state.eventSink(SettingsEvent.DismissSheet) },
        )

        SettingsSheet.NONE -> Unit
    }

    // ── Reset dialog ──────────────────────────────────────
    if (state.showResetDialog) {
        ResetConfirmDialog(
            onConfirm = { state.eventSink(SettingsEvent.ConfirmReset) },
            onDismiss = { state.eventSink(SettingsEvent.DismissReset) },
        )
    }
}
