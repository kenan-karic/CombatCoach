package io.aethibo.combatcoach.core.ui.extensions

import androidx.compose.runtime.saveable.Saver

inline fun <reified T : Enum<T>> enumSaver() = Saver<T, String>(
    save = { it.name },
    restore = { enumValueOf<T>(it) }
)
