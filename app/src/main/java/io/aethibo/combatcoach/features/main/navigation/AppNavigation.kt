package io.aethibo.combatcoach.features.main.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.aethibo.combatcoach.features.achievements.navigation.achievementsScreen
import io.aethibo.combatcoach.features.dashboard.navigation.dashboardScreen
import io.aethibo.combatcoach.features.dashboard.navigation.navigateToDashboard
import io.aethibo.combatcoach.features.onboarding.navigation.onboardingScreen
import io.aethibo.combatcoach.features.plan.navigation.plansScreen
import io.aethibo.combatcoach.features.plandetail.navigation.navigateToPlanDetails
import io.aethibo.combatcoach.features.plandetail.navigation.planDetailsScreen
import io.aethibo.combatcoach.features.progress.navigation.progressScreen
import io.aethibo.combatcoach.features.settings.navigation.settingsScreen
import io.aethibo.combatcoach.features.timer.navigation.navigateToTimer
import io.aethibo.combatcoach.features.timer.navigation.timerScreen
import io.aethibo.combatcoach.shared.utils.ItemType
import io.aethibo.combatcoachex.features.editcreate.presentation.navigation.createEditScreen
import io.aethibo.combatcoachex.features.editcreate.presentation.navigation.navigateToCreateEdit

@Composable
fun AppNavigation(startDestination: Any) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination

    val currentMainRoute = remember(currentDestination) {
        when {
            currentDestination?.hasRoute<Dashboard>() == true -> Dashboard
            currentDestination?.hasRoute<Plans>() == true -> Plans
            currentDestination?.hasRoute<Progress>() == true -> Progress
            currentDestination?.hasRoute<Achievements>() == true -> Achievements
            currentDestination?.hasRoute<Settings>() == true -> Settings
            else -> null
        }
    }

    val showBottomBar = currentMainRoute != null

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(
                    currentRoute = currentMainRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            // Pop up to the start destination to avoid building large back stack
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination
                            launchSingleTop = true
                            // Restore state when re-selecting a previously selected item
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(paddingValues),
            enterTransition = { slideInHorizontally(tween(300)) { it / 4 } + fadeIn(tween(300)) },
            exitTransition = { slideOutHorizontally(tween(300)) { -it / 4 } + fadeOut(tween(200)) },
            popEnterTransition = { slideInHorizontally(tween(300)) { -it / 4 } + fadeIn(tween(300)) },
            popExitTransition = { slideOutHorizontally(tween(300)) { it / 4 } + fadeOut(tween(200)) },
        ) {
            onboardingScreen(
                onNavigateToDashboard = navController::navigateToDashboard,
            )

            dashboardScreen(
                onNavigateToCreate = navController::navigateToCreateEdit,
                onNavigateToTimer = navController::navigateToTimer,
                onNavigateToPlan = navController::navigateToPlanDetails,
                onNavigateToWorkout = { workoutId ->
                    navController.navigateToCreateEdit(ItemType.Workout, workoutId)
                },
                onNavigateToCombo = { comboId ->
                    navController.navigateToCreateEdit(ItemType.Combo, comboId)
                }
            )

            plansScreen(
                onNavigateToPlan = navController::navigateToPlanDetails,
                onNavigateToCreate = { navController.navigateToCreateEdit(ItemType.Plan) }
            )

            planDetailsScreen(
                onBack = navController::navigateUp,
                onNavigateToEdit = { planId ->
                    navController.navigateToCreateEdit(ItemType.Plan, planId)
                },
                onNavigateToTimer = navController::navigateToTimer,
            )

            createEditScreen(
                onSaved = { navController.popBackStack() },
                onDeleted = { navController.popBackStack() },
                onBack = { navController.popBackStack() },
                onStartTimer = navController::navigateToTimer,
            )

            progressScreen()

            achievementsScreen()

            settingsScreen()

            timerScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
