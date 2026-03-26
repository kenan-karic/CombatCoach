package io.aethibo.combatcoach.core.ui.popup

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

internal data object PopupDefaults {
    val DefaultPopupShape
        @Composable
        get() = RoundedCornerShape(size = 8.dp)

    val DefaultPopupPadding
        @Composable
        get() = PaddingValues(all = 12.dp)

    val DefaultDismissIconSize
        @Composable
        get() = 24.dp

    val MaxIconSize
        @Composable
        get() = 40.dp

    val IconCircleBackgroundSize
        @Composable
        get() = 72.dp

    const val ButtonToBodyRatio = 0.75F
}
