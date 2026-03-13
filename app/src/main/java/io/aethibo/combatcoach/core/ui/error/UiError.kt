package io.aethibo.combatcoach.core.ui.error

import android.content.Context
import androidx.annotation.StringRes

// core/ui/UiError.kt
data class UiError(
    @param:StringRes val messageResId: Int,
    val args: List<Any> = emptyList(),
)

fun UiError.format(context: Context): String =
    if (args.isEmpty()) context.getString(messageResId)
    else context.getString(messageResId, *args.toTypedArray())
