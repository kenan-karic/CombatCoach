package io.aethibo.combatcoach.features.main

import androidx.compose.runtime.Immutable
import io.aethibo.combatcoach.core.failure.Failure
import io.aethibo.combatcoachex.features.shared.user.domain.model.ThemeMode

@Immutable
data class MainState(
    val isLoading: Boolean = true,
    val isOnboardingCompleted: Boolean = false,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val failure: Failure? = null,
)
