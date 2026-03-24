package io.aethibo.combatcoach.features.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import io.aethibo.combatcoach.shared.plan.domain.usecase.ClearActivePlanUseCase
import io.aethibo.combatcoach.shared.user.domain.model.UserPrefs
import io.aethibo.combatcoach.shared.user.domain.usecase.LoadUserPrefsUseCase
import io.aethibo.combatcoach.shared.user.domain.usecase.SaveUserPrefsUseCase
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch

@Composable
fun settingsPresenter(
    loadPrefs: LoadUserPrefsUseCase,
    savePrefs: SaveUserPrefsUseCase,
    clearActivePlan: ClearActivePlanUseCase,
): SettingsState {

    val scope = rememberCoroutineScope()

    var prefs by remember { mutableStateOf(UserPrefs()) }
    var isLoading by remember { mutableStateOf(true) }
    var isSaving by remember { mutableStateOf(false) }
    var activeSheet by remember { mutableStateOf(SettingsSheet.NONE) }
    var showResetDialog by remember { mutableStateOf(false) }

    // Load prefs once on entry
    LaunchedEffect(Unit) {
        loadPrefs()
            .fold(
                ifRight = { userPrefs ->
                    prefs = userPrefs
                },
                ifLeft = {

                }
            )
        isLoading = false
    }

    // Auto-save whenever prefs change — but skip the initial value.
    //
    //   Frame 1: LaunchedEffect(Unit) fires, calls loadPrefs(), sets prefs + isLoading = false
    //   Frame 1: LaunchedEffect(prefs) fires because prefs changed
    //   Race: if isLoading hasn't been written yet when the save effect reads it,
    //         the guard doesn't fire, and we save the default UserPrefs() to disk
    //         immediately on launch, potentially overwriting real stored prefs.
    //
    // snapshotFlow { prefs }.drop(1) is explicit and ordering-safe:
    //   - snapshotFlow captures the current value of `prefs` on each recomposition
    //   - drop(1) skips the first emission (the initial value set during load)
    //   - subsequent emissions are genuine user changes and should be saved
    LaunchedEffect(Unit) {
        snapshotFlow { prefs }
            .drop(1)  // skip initial load emission
            .collect { currentPrefs ->
                isSaving = true
                savePrefs(currentPrefs)
                isSaving = false
            }
    }

    // FIX: eventSink wrapped in remember { } — stable lambda reference.
    // Without this, every ToggleRow and PickerRow recomposes whenever any
    // pref changes (e.g. toggling sound invalidates the vibration row too).
    val eventSink: (SettingsEvent) -> Unit = remember {
        { event ->
            when (event) {
                is SettingsEvent.ThemeModeChanged -> prefs = prefs.copy(themeMode = event.mode)
                is SettingsEvent.DefaultDisciplineChanged -> prefs =
                    prefs.copy(defaultDiscipline = event.disc)

                is SettingsEvent.WeightUnitChanged -> prefs = prefs.copy(weightUnit = event.unit)
                is SettingsEvent.SoundToggled -> prefs = prefs.copy(soundEnabled = event.enabled)
                is SettingsEvent.VibrationToggled -> prefs =
                    prefs.copy(vibrationEnabled = event.enabled)

                is SettingsEvent.CountdownBeepsToggled -> prefs =
                    prefs.copy(countdownBeeps = event.enabled)

                SettingsEvent.OpenThemePicker -> activeSheet = SettingsSheet.THEME
                SettingsEvent.OpenDisciplinePicker -> activeSheet = SettingsSheet.DISCIPLINE
                SettingsEvent.OpenWeightUnitPicker -> activeSheet = SettingsSheet.WEIGHT_UNIT
                SettingsEvent.DismissSheet -> activeSheet = SettingsSheet.NONE

                SettingsEvent.RequestReset -> showResetDialog = true
                SettingsEvent.DismissReset -> showResetDialog = false

                SettingsEvent.ConfirmReset -> {
                    scope.launch {
                        clearActivePlan()
                        // Reset to defaults but preserve onboardingComplete — the user
                        // has already seen onboarding and resetting prefs shouldn't re-show it.
                        prefs = UserPrefs(onboardingComplete = true)
                        showResetDialog = false
                    }
                }
            }
        }
    }

    return SettingsState(
        prefs = prefs,
        isLoading = isLoading,
        isSaving = isSaving,
        activeSheet = activeSheet,
        showResetDialog = showResetDialog,
        appVersion = "1.0.0",
        eventSink = eventSink,
    )
}