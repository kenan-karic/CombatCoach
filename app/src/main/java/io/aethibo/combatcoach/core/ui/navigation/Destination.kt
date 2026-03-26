package io.aethibo.combatcoach.core.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import kotlinx.serialization.Serializable

/**
 * The base class for all navigation routes in the application.
 * Classes or objects inheriting from this must be marked with @Serializable
 * to support Jetpack Compose Type-Safe navigation.
 */
@Serializable
open class Destination {
    /**
     * Determines if this destination should show the bottom navigation bar.
     */
    open val showBottomBar: Boolean = false
}

/**
 * The bridge between Shared KMM logic and the Android-specific NavController.
 * This class translates [NavigationAction]s into actual Android framework calls.
 * @param navController The Jetpack Compose NavController managing the current backstack.
 * @param context Required for platform-specific actions like opening URLs.
 */
class NavigationHandler(
    private val navController: NavController,
//    private val context: Context
) {
    /**
     * The primary entry point for navigation triggered by business logic.
     * Use this when your ViewModel emits a navigation intent.
     */
    fun handle(action: NavigationAction) {
        when (action) {
            is NavigationAction.Navigate -> {
                navController.navigate(action.route)
            }

            is NavigationAction.NavigateAndClearStack -> {
                navController.navigate(action.route) {
                    popUpTo(0) { inclusive = true }
                }
            }

            is NavigationAction.NavigateAndPopUpTo -> {
                navController.navigate(action.route) {
                    popUpTo(action.popUpToRoute) {
                        inclusive = action.inclusive
                    }
                }
            }

            is NavigationAction.Back -> navController.navigateUp()

            is NavigationAction.OpenUrl -> {
//                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(action.url)).apply {
//                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                }
//                context.startActivity(intent)
            }
        }
    }

    /**
     * A type-safe convenience method for UI-driven navigation.
     * Example: `navigationHandler.navigateTo(ProfileRoute) { launchSingleTop = true }`
     */
    fun <T : Destination> navigateTo(
        route: T,
        builder: NavOptionsBuilder.() -> Unit = {}
    ) = navController.navigate(route, builder)

    /** Performs a standard "Up" or "Back" navigation. */
    fun navigateBack() = navController.navigateUp()
}
