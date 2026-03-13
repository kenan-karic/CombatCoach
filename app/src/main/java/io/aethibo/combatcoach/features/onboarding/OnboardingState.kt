package io.aethibo.combatcoach.features.onboarding

import androidx.compose.runtime.Stable

@Stable
data class OnboardingState(
    val pages: List<OnboardingPage> = emptyList(),
    val eventSink: (OnboardingEvent) -> Unit = {},
)
