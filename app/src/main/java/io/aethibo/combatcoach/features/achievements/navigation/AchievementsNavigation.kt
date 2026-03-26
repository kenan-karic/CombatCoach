package io.aethibo.combatcoach.features.achievements.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.aethibo.combatcoach.features.achievements.AchievementsScreen
import io.aethibo.combatcoach.features.achievements.achievementsPresenter
import io.aethibo.combatcoach.features.main.navigation.Achievements
import org.koin.compose.koinInject

fun NavController.navigateToAchievements() {
    navigate(Achievements) {
        popUpTo(0) { inclusive = true }
    }
}

fun NavGraphBuilder.achievementsScreen() {
    composable<Achievements> {
        val state = achievementsPresenter(
            observeAchievements = koinInject(),
        )

        AchievementsScreen(state = state)
    }
}
