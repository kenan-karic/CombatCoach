package io.aethibo.combatcoach.shared.user.domain.model

import io.aethibo.combatcoach.shared.utils.Discipline

data class UserPrefs(
    val onboardingComplete: Boolean = false,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val defaultDiscipline: Discipline = Discipline.GENERAL,
    val soundEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val weightUnit: WeightUnit = WeightUnit.KG,
    val countdownBeeps: Boolean = true,
)