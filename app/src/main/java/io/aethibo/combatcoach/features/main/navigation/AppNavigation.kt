package io.aethibo.combatcoach.features.main.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import io.aethibo.combatcoach.features.onboarding.navigation.onboardingScreen

@Composable
fun AppNavigation(startDestination: Any) {
    val navController = rememberNavController()

    Scaffold { paddingValues ->
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
                onNavigateToHome = {
                },
            )
        }
    }
}
