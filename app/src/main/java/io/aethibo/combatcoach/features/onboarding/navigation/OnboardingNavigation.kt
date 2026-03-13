package io.aethibo.combatcoach.features.onboarding.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.aethibo.combatcoach.core.ui.navigation.Destination
import io.aethibo.combatcoach.features.onboarding.OnboardingScreen
import io.aethibo.combatcoach.features.onboarding.onboardingPresenter
import kotlinx.serialization.Serializable
import org.koin.compose.koinInject

@Serializable
object OnboardingRoute : Destination()

fun NavGraphBuilder.onboardingScreen(
    onNavigateToHome: () -> Unit,
) {
    composable<OnboardingRoute> {
        val state = onboardingPresenter(
            savePrefs = koinInject(),
            loadPrefs = koinInject(),
            onFinished = onNavigateToHome,
        )
        OnboardingScreen(state = state)
    }
}
