package io.aethibo.combatcoach.features.timer.navigation

import androidx.compose.material3.Text
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import io.aethibo.combatcoach.features.timer.combat.CombatTimerScreen
import io.aethibo.combatcoach.features.timer.combat.combatTimerPresenter
import io.aethibo.combatcoach.shared.utils.ItemType
import io.aethibo.combatcoach.shared.utils.ItemTypeNavType
import kotlinx.serialization.Serializable
import org.koin.compose.koinInject
import kotlin.reflect.typeOf

@Serializable
data class TimerRoute(
    val targetType: ItemType,
    val targetId: Int,
)

val timerTypeMap = mapOf(typeOf<ItemType>() to ItemTypeNavType)

fun NavController.navigateToTimer(type: ItemType, id: Int) {
    navigate(TimerRoute(type, id))
}

fun NavGraphBuilder.timerScreen(
    onBack: () -> Unit,
) {
    composable<TimerRoute>(typeMap = timerTypeMap) { backStackEntry ->
        val route = backStackEntry.toRoute<TimerRoute>()

        when (route.targetType) {
            ItemType.Workout -> {
                WorkoutTimerRouter(
                    workoutId = route.targetId,
                    observeWorkoutById = koinInject(),
                    observeWorkoutLogs = koinInject(),
                    saveWorkoutLog = koinInject(),
                    checkAchievements = koinInject(),
                    loadPrefs = koinInject(),
                    repository = koinInject(),
                    soundCoordinator = koinInject(),
                    onBack = onBack,
                )
            }

            ItemType.Combo -> {
                val state = combatTimerPresenter(
                    comboId = route.targetId,
                    observeComboById = koinInject(),
                    saveWorkoutLog = koinInject(),
                    checkAchievements = koinInject(),
                    loadPrefs = koinInject(),
                    repository = koinInject(),
                    soundCoordinator = koinInject(),
                    onBack = onBack,
                )

                CombatTimerScreen(state = state, onBack = onBack)
            }

            ItemType.Plan -> Text("Plans do not support timers")
        }
    }
}
