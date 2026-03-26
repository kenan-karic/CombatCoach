package io.aethibo.combatcoach.features.plan.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.aethibo.combatcoach.features.main.navigation.Plans
import io.aethibo.combatcoach.features.plan.PlansScreen
import io.aethibo.combatcoach.features.plan.plansPresenter
import org.koin.compose.koinInject

fun NavController.navigateToPlans() {
    navigate(Plans) {
        popUpTo(0) { inclusive = true }
    }
}

fun NavGraphBuilder.plansScreen(
    onNavigateToPlan: (Int) -> Unit,
    onNavigateToCreate: () -> Unit,
) {
    composable<Plans> {
        val state = plansPresenter(
            onNavigateToPlan = onNavigateToPlan,
            onNavigateToCreate = onNavigateToCreate,
            observePlans = koinInject(),
            observeActivePlan = koinInject()

        )

        PlansScreen(state = state)
    }
}
