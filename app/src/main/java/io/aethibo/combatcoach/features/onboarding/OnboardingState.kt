package io.aethibo.combatcoach.features.onboarding

import androidx.compose.runtime.Stable
import io.aethibo.combatcoach.features.onboarding.model.OnboardingPage

@Stable
data class OnboardingState(
    val pages: List<OnboardingPage> = emptyList(),
    val eventSink: (OnboardingEvent) -> Unit = {},
)
