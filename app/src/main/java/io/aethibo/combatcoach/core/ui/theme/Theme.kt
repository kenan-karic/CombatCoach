package io.aethibo.combatcoach.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = DarkPeriwinkle,
    onPrimary = Charcoal,
    primaryContainer = DeepPeriwinkle,
    onPrimaryContainer = DarkPeriwinkle.copy(alpha = 0.8f),

    secondary = CoralPink,
    onSecondary = Charcoal,
    secondaryContainer = DeepCoral,
    onSecondaryContainer = CoralPink.copy(alpha = 0.8f),

    tertiary = MintGreen,
    onTertiary = Charcoal,
    tertiaryContainer = MintGreen.copy(alpha = 0.3f),
    onTertiaryContainer = MintGreen.copy(alpha = 0.8f),

    background = DarkBackground,
    onBackground = SurfaceWhite.copy(alpha = 0.87f),

    surface = DarkSurface,
    onSurface = SurfaceWhite.copy(alpha = 0.87f),
    surfaceVariant = DarkSurface,
    onSurfaceVariant = SurfaceWhite.copy(alpha = 0.6f),

    error = SoftRed.copy(alpha = 0.9f),
    onError = SurfaceWhite,

    outline = SurfaceWhite.copy(alpha = 0.3f),
    outlineVariant = SurfaceWhite.copy(alpha = 0.12f)
)

private val LightColorScheme = lightColorScheme(
    primary = Periwinkle,
    onPrimary = SurfaceWhite,
    primaryContainer = Periwinkle.copy(alpha = 0.12f),
    onPrimaryContainer = DeepPeriwinkle,

    secondary = CoralPink,
    onSecondary = SurfaceWhite,
    secondaryContainer = CoralPink.copy(alpha = 0.12f),
    onSecondaryContainer = DeepCoral,

    tertiary = MintGreen,
    onTertiary = SurfaceWhite,
    tertiaryContainer = MintGreen.copy(alpha = 0.12f),
    onTertiaryContainer = MintGreen,

    background = CloudWhite,
    onBackground = Charcoal,

    surface = SurfaceWhite,
    onSurface = Charcoal,
    surfaceVariant = CloudWhite,
    onSurfaceVariant = MediumGray,

    error = SoftRed,
    onError = SurfaceWhite,

    outline = MediumGray.copy(alpha = 0.3f),
    outlineVariant = MediumGray.copy(alpha = 0.12f)
)

@Composable
fun CombatCoachTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = CombatShapes,
        content = content
    )
}