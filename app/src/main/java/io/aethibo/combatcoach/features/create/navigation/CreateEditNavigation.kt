package io.aethibo.combatcoach.features.create.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import io.aethibo.combatcoach.features.create.utils.CreateEditMode
import io.aethibo.combatcoach.shared.utils.ItemType
import io.aethibo.combatcoach.shared.utils.ItemTypeNavType
import io.aethibo.combatcoachex.features.editcreate.presentation.combo.ComboEditScreen
import io.aethibo.combatcoachex.features.editcreate.presentation.combo.comboEditPresenter
import io.aethibo.combatcoachex.features.editcreate.presentation.model.CreateEditMode
import io.aethibo.combatcoachex.features.editcreate.presentation.model.ItemType
import io.aethibo.combatcoachex.features.editcreate.presentation.model.ItemTypeNavType
import io.aethibo.combatcoachex.features.editcreate.presentation.plan.PlanEditScreen
import io.aethibo.combatcoachex.features.editcreate.presentation.planEditPresenter
import io.aethibo.combatcoachex.features.editcreate.presentation.wokrout.WorkoutEditScreen
import io.aethibo.combatcoachex.features.editcreate.presentation.workoutEditPresenter
import kotlinx.serialization.Serializable
import org.koin.compose.koinInject
import kotlin.reflect.typeOf

/**
 * Route for the create/edit screen.
 *
 * [itemId] is null when creating a new item, or the Room Int PK when editing.
 * It is typed as [Int?] (not String?) to match the domain model ID type and
 * avoid any UUID ↔ Int conversion bugs.
 */
@Serializable
data class CreateEditRoute(
    val itemType: ItemType,
    val itemId: Int? = null,
)

/**
 * Shared typeMap — must be passed to both [composable] and [toRoute] calls.
 *
 * Compose Navigation cannot derive a NavType for sealed interfaces automatically,
 * even when they are @Serializable.  [ItemTypeNavType] handles JSON
 * serialisation so the value survives process death and back-stack restoration.
 */
val createEditTypeMap = mapOf(typeOf<ItemType>() to ItemTypeNavType)

fun NavController.navigateToCreateEdit(
    itemType: ItemType,
    itemId: Int? = null,
) {
    navigate(CreateEditRoute(itemType, itemId))
}

fun NavGraphBuilder.createEditScreen(
    onSaved: () -> Unit,
    onDeleted: () -> Unit,
    onBack: () -> Unit,
    onStartTimer: ((ItemType, String) -> Unit)?,
) {
    composable<CreateEditRoute>(typeMap = createEditTypeMap) { backStackEntry ->
        val route = backStackEntry.toRoute<CreateEditRoute>()

        val mode: CreateEditMode = route.itemId
            ?.let { CreateEditMode.Edit(it) }
            ?: CreateEditMode.Create

        when (route.itemType) {
            ItemType.Workout -> {
                val state = workoutEditPresenter(
                    mode = mode,
                    observeWorkoutById = koinInject(),
                    saveWorkout = koinInject(),
                    deleteWorkout = koinInject(),
                    onSaved = onSaved,
                    onDeleted = onDeleted,
                    onStartTimer = onStartTimer,
                    onBack = onBack,
                )
                WorkoutEditScreen(state = state, onBack = onBack)
            }

            ItemType.Combo -> {
                val state = comboEditPresenter(
                    mode = mode,
                    observeComboById = koinInject(),
                    saveCombo = koinInject(),
                    deleteCombo = koinInject(),
                    onSaved = onSaved,
                    onDeleted = onDeleted,
                    onStartTimer = onStartTimer,
                )
                ComboEditScreen(state, onBack)
            }

            ItemType.Plan -> {
                val state = planEditPresenter(
                    mode = mode,
                    observePlanById = koinInject(),
                    observeWorkouts = koinInject(),
                    observeCombos = koinInject(),
                    savePlan = koinInject(),
                    deletePlan = koinInject(),
                    onSaved = onSaved,
                    onDeleted = onDeleted,
                )
                PlanEditScreen(state, onBack)
            }
        }
    }
}
