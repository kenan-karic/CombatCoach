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
import androidx.compose.material.icons.automirrored.outlined.VolumeUp
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
import androidx.compose.ui.res.stringResource
import io.aethibo.combatcoach.R
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing
import io.aethibo.combatcoach.features.settings.components.ActionRow
import io.aethibo.combatcoach.features.settings.components.AppBrandingFooter
import io.aethibo.combatcoach.features.settings.components.AppInfoRow
import io.aethibo.combatcoach.features.settings.components.DisciplinePickerSheet
import io.aethibo.combatcoachex.features.settings.presentation.components.ActionRow
import io.aethibo.combatcoachex.features.settings.presentation.components.AppInfoRow
import io.aethibo.combatcoachex.features.settings.presentation.components.DisciplinePickerSheet
import io.aethibo.combatcoach.features.settings.components.PickerRow
import io.aethibo.combatcoachex.features.settings.presentation.components.ResetConfirmDialog
import io.aethibo.combatcoach.features.settings.components.SettingsDivider
import io.aethibo.combatcoach.features.settings.components.SettingsHeader
import io.aethibo.combatcoach.features.settings.components.SettingsSection
import io.aethibo.combatcoach.features.settings.components.ThemePickerSheet
import io.aethibo.combatcoachex.features.settings.presentation.components.ThemePickerSheet
import io.aethibo.combatcoach.features.settings.components.ToggleRow
import io.aethibo.combatcoach.features.settings.components.WeightUnitPickerSheet
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
        SettingsSection(titleResId = R.string.settings_section_appearance) {
            PickerRow(
                icon = Icons.Outlined.Palette,
                label = stringResource(R.string.settings_label_theme),
                value = state.prefs.themeMode.label(),
                onClick = { state.eventSink(SettingsEvent.OpenThemePicker) },
            )
        }

        // ── Training defaults ──────────────────────────────
        SettingsSection(titleResId = R.string.settings_section_training) {
            PickerRow(
                icon = Icons.Outlined.SportsMartialArts,
                label = stringResource(R.string.settings_label_discipline),
                value = state.prefs.defaultDiscipline.label(),
                onClick = { state.eventSink(SettingsEvent.OpenDisciplinePicker) },
            )
            SettingsDivider()
            PickerRow(
                icon = Icons.Outlined.FitnessCenter,
                label = stringResource(R.string.settings_label_weight_unit),
                value = state.prefs.weightUnit.label(),
                onClick = { state.eventSink(SettingsEvent.OpenWeightUnitPicker) },
            )
        }

        // ── Audio & Haptics ────────────────────────────────
        SettingsSection(titleResId = R.string.settings_section_audio_haptic) {
            ToggleRow(
                icon = Icons.AutoMirrored.Outlined.VolumeUp,
                label = stringResource(R.string.settings_label_sound),
                subLabel = stringResource(R.string.settings_sub_sound),
                checked = state.prefs.soundEnabled,
                onToggle = { state.eventSink(SettingsEvent.SoundToggled(it)) },
            )
            SettingsDivider()
            ToggleRow(
                icon = Icons.Outlined.Vibration,
                label = stringResource(R.string.settings_label_vibration),
                subLabel = stringResource(R.string.settings_sub_vibration),
                checked = state.prefs.vibrationEnabled,
                onToggle = { state.eventSink(SettingsEvent.VibrationToggled(it)) },
            )
            SettingsDivider()
            ToggleRow(
                icon = Icons.Outlined.Timer,
                label = stringResource(R.string.settings_label_countdown),
                subLabel = stringResource(R.string.settings_sub_countdown),
                checked = state.prefs.countdownBeeps,
                onToggle = { state.eventSink(SettingsEvent.CountdownBeepsToggled(it)) },
            )
        }

        // ── Data ───────────────────────────────────────────
        SettingsSection(titleResId = R.string.settings_section_data) {
            ActionRow(
                icon = Icons.Outlined.RestartAlt,
                label = stringResource(R.string.settings_label_reset),
                subLabel = stringResource(R.string.settings_sub_reset),
                color = MaterialTheme.colorScheme.error,
                onClick = { state.eventSink(SettingsEvent.RequestReset) },
            )
        }

        // ── About ──────────────────────────────────────────
        SettingsSection(titleResId = R.string.settings_section_about) {
            AppInfoRow(
                icon = Icons.Outlined.Info,
                label = stringResource(R.string.settings_info_version),
                value = state.appVersion,
            )
            SettingsDivider()
            AppInfoRow(
                icon = Icons.Outlined.Shield,
                label = stringResource(R.string.settings_label_storage),
                value = stringResource(R.string.settings_value_local_only),
            )
            SettingsDivider()
            AppInfoRow(
                icon = Icons.Outlined.CloudOff,
                label = stringResource(R.string.settings_label_network),
                value = stringResource(R.string.settings_value_offline_only),
            )
            SettingsDivider()
            AppInfoRow(
                icon = Icons.Outlined.Lock,
                label = stringResource(R.string.settings_label_account),
                value = stringResource(R.string.settings_value_none),
            )
        }

        // ── App branding footer ────────────────────────────
        AppBrandingFooter()

        Spacer(Modifier.height(sp.xxl))
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
