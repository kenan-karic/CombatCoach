package io.aethibo.combatcoach.features.plan

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import io.aethibo.combatcoach.features.plan.model.PlansData
import io.aethibo.combatcoach.shared.plan.domain.usecase.ObserveActivePlanUseCase
import io.aethibo.combatcoach.shared.plan.domain.usecase.ObservePlansUseCase
import kotlinx.coroutines.flow.combine

@Composable
fun plansPresenter(
    observePlans: ObservePlansUseCase,
    observeActivePlan: ObserveActivePlanUseCase,
    onNavigateToPlan: (Int) -> Unit,
    onNavigateToCreate: () -> Unit,
): PlansState {

    val data by remember {
        combine(
            observePlans(),
            observeActivePlan(),
        ) { plans, active -> PlansData(plans, active) }
    }.collectAsState(initial = PlansData())

    val activePlanDetail = remember(data.plans, data.activePlan) {
        data.plans.find { it.id == data.activePlan?.planId }
    }

    val eventSink: (PlansEvent) -> Unit = remember {
        { event ->
            when (event) {
                is PlansEvent.OpenPlan -> onNavigateToPlan(event.planId)
                PlansEvent.CreatePlan -> onNavigateToCreate()
            }
        }
    }

    return PlansState(
        allPlans = data.plans,
        activePlan = data.activePlan,
        activePlanDetail = activePlanDetail,
        isLoading = false,
        eventSink = eventSink,
    )
}
