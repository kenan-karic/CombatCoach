package io.aethibo.combatcoach.features.onboarding.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.aethibo.combatcoach.core.ui.navigation.Destination
import io.aethibo.combatcoach.features.onboarding.OnboardingScreen
import kotlinx.serialization.Serializable

@Serializable
object OnboardingRoute : Destination()

fun NavGraphBuilder.onboardingScreen(
    onNavigateToHome: () -> Unit,
) {
    composable<OnboardingRoute> {
        val state = onboardingPresenter(
            onFinished = onNavigateToHome,
        )
        OnboardingScreen(state = state)
    }
}