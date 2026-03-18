package io.aethibo.combatcoach.features.main.navigation

import io.aethibo.combatcoach.core.ui.navigation.Destination
import kotlinx.serialization.Serializable

@Serializable
object Home : Destination()

@Serializable
object Plans : Destination()

@Serializable
object Progress : Destination()

@Serializable
object Achievements : Destination()

@Serializable
object Settings : Destination()
