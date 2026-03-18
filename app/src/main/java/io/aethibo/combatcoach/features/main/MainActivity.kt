package io.aethibo.combatcoach.features.main

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import io.aethibo.combatcoach.core.ui.navigation.Destination
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme
import io.aethibo.combatcoach.features.main.navigation.AppNavigation
import io.aethibo.combatcoach.features.onboarding.navigation.OnboardingRoute
import io.aethibo.combatcoach.shared.user.domain.model.ThemeMode
import kotlinx.serialization.Serializable
import org.koin.compose.koinInject

@Serializable
object TempRoute : Destination()

class MainActivity : ComponentActivity() {

    private val splashStateHolder = SplashStateHolder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setupSplashScreen()

        setContent {
            CombatCoachApp()
        }
    }

    @Composable
    private fun CombatCoachApp() {
        val state = mainPresenter(
            splashStateHolder = splashStateHolder,
            loadPrefs = koinInject()
        )

        val darkTheme = when (state.themeMode) {
            ThemeMode.DARK -> true
            ThemeMode.LIGHT -> false
            ThemeMode.SYSTEM -> isSystemInDarkTheme()
        }

        CombatCoachTheme(darkTheme = darkTheme) {
            when {
                state.isLoading -> Unit
                else -> AppNavigation(startDestination = startDestinationFor(state))
            }
        }
    }

    private fun startDestinationFor(state: MainState): Any = when {
        !state.isOnboardingCompleted -> OnboardingRoute
        else -> TempRoute
    }

    private fun setupSplashScreen() {
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { splashStateHolder.isLoading.value }
        splashScreen.setOnExitAnimationListener { splashScreenView ->
            ObjectAnimator.ofFloat(splashScreenView.view, View.ALPHA, 1f, 0f).apply {
                duration = 300L
                doOnEnd { splashScreenView.remove() }
                start()
            }
        }
    }
}
