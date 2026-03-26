package io.aethibo.combatcoach.core.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class CCSpacing(
    val xxs: Dp = 4.dp,
    val xs: Dp = 8.dp,
    val small: Dp = 12.dp,
    val medium: Dp = 16.dp,
    val large: Dp = 24.dp,
    val xl: Dp = 32.dp,
    val xxl: Dp = 48.dp,
    val xxxl: Dp = 64.dp,

    // Named semantic aliases
    val screenPadding: Dp = 24.dp,
    val cardPadding: Dp = 20.dp,
    val cardGap: Dp = 12.dp,
    val sectionGap: Dp = 32.dp,
    val itemGap: Dp = 8.dp,
)

val LocalSpacing = staticCompositionLocalOf { CCSpacing() }
