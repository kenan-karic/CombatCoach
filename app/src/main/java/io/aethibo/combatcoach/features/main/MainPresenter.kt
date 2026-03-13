package io.aethibo.combatcoach.features.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.aethibo.combatcoach.core.failure.Failure
import io.aethibo.combatcoach.features.shared.user.domain.usecase.LoadUserPrefsUseCase
import io.aethibo.combatcoachex.features.shared.user.domain.model.ThemeMode

@Composable
fun mainPresenter(
    splashStateHolder: SplashStateHolder,
    loadPrefs: LoadUserPrefsUseCase,
): MainState {

    var isLoading by remember { mutableStateOf(true) }
    var isOnboardingCompleted by remember { mutableStateOf(false) }
    var themeMode by remember { mutableStateOf(ThemeMode.SYSTEM) }
    var failure by remember { mutableStateOf<Failure?>(null) }

    LaunchedEffect(Unit) {
        loadPrefs()
            .onRight { prefs ->
                isOnboardingCompleted = prefs.onboardingComplete
                themeMode = prefs.themeMode
            }
            .onLeft { f ->
                failure = f
            }

        isLoading = false
        splashStateHolder.onLoadingComplete()
    }

    return MainState(
        isLoading = isLoading,
        isOnboardingCompleted = isOnboardingCompleted,
        themeMode = themeMode,
        failure = failure,
    )
}
