package io.aethibo.combatcoach.features.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.aethibo.combatcoach.features.main.navigation.Settings
import io.aethibo.combatcoach.features.settings.SettingsScreen
import io.aethibo.combatcoach.features.settings.settingsPresenter
import org.koin.compose.koinInject

fun NavController.navigateToSettings() {
    navigate(Settings) {
        popUpTo(0) { inclusive = true }
    }
}

fun NavGraphBuilder.settingsScreen() {
    composable<Settings> {
        val state = settingsPresenter(
            loadPrefs = koinInject(),
            savePrefs = koinInject(),
            clearActivePlan = koinInject(),
        )

        SettingsScreen(state = state)
    }
}
