package io.aethibo.combatcoach.features.onboarding

sealed interface OnboardingEvent {
    data object Finish : OnboardingEvent
    data object Skip : OnboardingEvent
}
