package io.aethibo.combatcoach.features.plandetail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import io.aethibo.combatcoach.core.ui.navigation.Destination
import io.aethibo.combatcoach.features.main.navigation.Plans
import io.aethibo.combatcoach.features.plandetail.PlanDetailScreen
import io.aethibo.combatcoach.features.plandetail.planDetailPresenter
import io.aethibo.combatcoach.shared.utils.ItemType
import kotlinx.serialization.Serializable
import org.koin.compose.koinInject

@Serializable
data class PlanDetailsRoute(val id: Int): Destination()

fun NavController.navigateToPlanDetails(id: Int) {
    navigate(PlanDetailsRoute(id)) {
        popUpTo(Plans) { inclusive = false }
    }
}

fun NavGraphBuilder.planDetailsScreen(
    onBack: () -> Unit,
    onNavigateToTimer: (ItemType, Int) -> Unit,
    onNavigateToEdit: (Int) -> Unit,
) {
    composable<PlanDetailsRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<PlanDetailsRoute>()
        val state = planDetailPresenter(
            planId = route.id,
            onNavigateToTimer = onNavigateToTimer,
            onNavigateToEdit = onNavigateToEdit,
            observePlanById = koinInject(),
            observeWorkouts = koinInject(),
            observeCombos = koinInject(),
            observeActivePlan = koinInject(),
            setActivePlan = koinInject(),
            clearActivePlan = koinInject(),
            advanceActivePlanDay = koinInject(),
        )

        PlanDetailScreen(state = state, onBack = onBack)
    }
}
