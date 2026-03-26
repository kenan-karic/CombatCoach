package io.aethibo.combatcoach.features.dashboard.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.aethibo.combatcoach.features.dashboard.DashboardScreen
import io.aethibo.combatcoach.features.dashboard.dashboardPresenter
import io.aethibo.combatcoach.features.main.navigation.Dashboard
import io.aethibo.combatcoach.shared.utils.ItemType
import org.koin.compose.koinInject

fun NavController.navigateToDashboard() {
    navigate(Dashboard) {
        popUpTo(0) { inclusive = true }
    }
}

fun NavGraphBuilder.dashboardScreen(
    onNavigateToCreate: (ItemType) -> Unit,
    onNavigateToWorkout: (Int) -> Unit,
    onNavigateToCombo: (Int) -> Unit,
    onNavigateToPlan: (Int) -> Unit,
    onNavigateToTimer: (ItemType, Int) -> Unit,
) {
    composable<Dashboard> {
        val state = dashboardPresenter(
            onNavigateToCreate = onNavigateToCreate,
            onNavigateToWorkout = onNavigateToWorkout,
            onNavigateToPlan = onNavigateToPlan,
            onNavigateToTimer = onNavigateToTimer,
            onNavigateToCombo = onNavigateToCombo,
            observeWorkouts = koinInject(),
            observeCombos = koinInject(),
            observePlans = koinInject(),
            observeActivePlan = koinInject(),
            observeDashboardStats = koinInject(),
        )
        DashboardScreen(state = state)
    }
}
