package io.aethibo.combatcoach.features.onboarding.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

@Stable
data class OnboardingPage(
    @param:StringRes val titleRes: Int,
    @param:StringRes val subtitleRes: Int,
    @param:StringRes val descriptionRes: Int,
    val illustrationKey: IllustrationKey,
    val accentColor: Color,
)
