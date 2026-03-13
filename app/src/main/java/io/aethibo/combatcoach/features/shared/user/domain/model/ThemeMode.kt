package io.aethibo.combatcoachex.features.shared.user.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BrightnessAuto
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.ui.graphics.vector.ImageVector

enum class ThemeMode {
    SYSTEM, LIGHT, DARK;

    fun label(): String = when (this) {
        SYSTEM -> "System default"
        LIGHT -> "Light"
        DARK -> "Dark"
    }

    fun icon(): ImageVector = when (this) {
        ThemeMode.SYSTEM -> Icons.Outlined.BrightnessAuto
        ThemeMode.LIGHT -> Icons.Outlined.LightMode
        ThemeMode.DARK -> Icons.Outlined.DarkMode
    }
}
