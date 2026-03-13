package io.aethibo.combatcoach.core.ui.navigation

/**
 * Represents high-level navigation intents that can be triggered from
 * either the UI or Business Logic (Shared ViewModels).
 */
sealed interface NavigationAction {
    /** Navigates to a new destination, adding it to the backstack. */
    data class Navigate(val route: Destination) : NavigationAction

    /** Navigates to a destination and clears the entire backstack. */
    data class NavigateAndClearStack(val route: Destination) : NavigationAction

    /** Navigates to a destination while popping the stack up to a specific route. */
    data class NavigateAndPopUpTo(
        val route: Destination,
        val popUpToRoute: Destination,
        val inclusive: Boolean = false
    ) : NavigationAction

    /** Performs a standard "Up" or "Back" navigation. */
    data object Back : NavigationAction

    /** Triggers an external browser intent to open a specific URL. */
    data class OpenUrl(val url: String) : NavigationAction
}
