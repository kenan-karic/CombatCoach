package io.aethibo.combatcoach.features.progress.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.aethibo.combatcoach.features.main.navigation.Progress
import io.aethibo.combatcoach.features.progress.ProgressScreen
import io.aethibo.combatcoach.features.progress.progressPresenter
import org.koin.compose.koinInject

fun NavController.navigateToProgress() {
    navigate(Progress) {
        popUpTo(0) { inclusive = true }
    }
}

fun NavGraphBuilder.progressScreen() {
    composable<Progress> {
        val state = progressPresenter(
            observeLogs = koinInject(),
            observeWorkouts = koinInject(),
            observeCombos = koinInject(),
            observeDashboard = koinInject(),
            deleteLog = koinInject(),
        )

        ProgressScreen(state = state)
    }
}
