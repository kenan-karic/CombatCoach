package io.aethibo.combatcoach.core.ui.popup

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import io.aethibo.combatcoach.R
import io.aethibo.combatcoach.core.ui.theme.Charcoal
import io.aethibo.combatcoach.core.ui.theme.Crimson
import io.aethibo.combatcoach.core.ui.theme.SuccessGreen
import io.aethibo.combatcoach.core.ui.theme.WarningOrange

data object PopupTypes {
    val Generic = PopupType(
        resId = R.drawable.ic_generic,
        background = Charcoal
    )
    val Info = PopupType(
        resId = R.drawable.ic_info,
        background = Charcoal
    )
    val Success = PopupType(
        resId = R.drawable.ic_success,
        background = SuccessGreen
    )
    val Warning = PopupType(
        resId = R.drawable.ic_warning,
        background = WarningOrange
    )
    val Error = PopupType(
        resId = R.drawable.ic_error,
        background = Crimson
    )
}

data class PopupType(
    @param:DrawableRes val resId: Int,
    val background: Color
)
