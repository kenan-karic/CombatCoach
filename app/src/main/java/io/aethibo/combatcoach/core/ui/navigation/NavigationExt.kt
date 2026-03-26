package io.aethibo.combatcoach.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf

/**
 * A CompositionLocal used to provide the [NavigationHandler] down the Compose tree.
 * This allows any screen or component to access navigation without passing it through parameters.
 */
val LocalNavigationHandler = compositionLocalOf<NavigationHandler> {
    error("No NavigationHandler provided. Ensure your AppNavigation provides this through CompositionLocalProvider.")
}

/**
 * A helper function to easily retrieve the current [NavigationHandler] within a @Composable.
 */
@Composable
fun rememberNavigationHandler(): NavigationHandler {
    return LocalNavigationHandler.current
}
