package io.aethibo.combatcoach.features.plan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing
import io.aethibo.combatcoach.features.plan.components.ActivePlanBanner
import io.aethibo.combatcoach.features.plan.components.PlanCard
import io.aethibo.combatcoach.features.plan.components.PlansEmptyState
import io.aethibo.combatcoach.shared.plan.domain.model.ActivePlan
import io.aethibo.combatcoach.shared.plan.domain.model.Plan
import io.aethibo.combatcoach.shared.plan.domain.model.PlanType
import io.aethibo.combatcoach.shared.utils.Discipline

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlansScreen(state: PlansState) {
    val sp = LocalSpacing.current

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Plans", style = MaterialTheme.typography.headlineMedium) },
                actions = {
                    IconButton(onClick = { state.eventSink(PlansEvent.CreatePlan) }) {
                        Icon(Icons.Filled.Add, "Create plan")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
            )
        },
    ) { padding ->
        if (state.isLoading) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                Alignment.Center,
            ) { CircularProgressIndicator(color = MaterialTheme.colorScheme.primary) }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(horizontal = sp.screenPadding, vertical = sp.medium),
            verticalArrangement = Arrangement.spacedBy(sp.cardGap),
        ) {
            state.activePlanDetail?.let { active ->
                item(key = "active-banner") {
                    ActivePlanBanner(
                        plan = active,
                        activePlan = state.activePlan ?: return@item,
                        onClick = { state.eventSink(PlansEvent.OpenPlan(active.id)) },
                    )
                    Spacer(Modifier.height(sp.sectionGap))
                }
            }

            // ── Section header ─────────────────────────────────────────────
            if (state.hasPlans) {
                item(key = "header") {
                    Text(
                        text = state.sectionTitle,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            // ── Plan list or empty state ───────────────────────────────────
            if (!state.hasPlans) {
                item(key = "empty") {
                    PlansEmptyState(
                        onCreate = { state.eventSink(PlansEvent.CreatePlan) },
                    )
                }
            } else {
                items(state.allPlans, key = { it.id }) { plan ->
                    PlanCard(
                        plan = plan,
                        isActive = plan.id == state.activePlan?.planId,
                        activePlan = if (plan.id == state.activePlan?.planId) state.activePlan else null,
                        onClick = { state.eventSink(PlansEvent.OpenPlan(plan.id)) },
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Loading")
@Composable
private fun PlansLoadingPreview() {
    CombatCoachTheme {
        PlansScreen(
            state = PlansState(
                isLoading = true
            )
        )
    }
}

@Preview(showBackground = true, name = "Empty")
@Composable
private fun PlansEmptyPreview() {
    CombatCoachTheme {
        PlansScreen(
            state = PlansState(
                isLoading = false
            )
        )
    }
}

@Preview(showBackground = true, name = "With active plan")
@Composable
private fun PlansWithActivePreview() {
    CombatCoachTheme {
        val previewPlan = Plan(
            id = 1,
            name = "8-Week Boxing Program",
            planType = PlanType.PROGRAM,
            discipline = Discipline.STRIKING,
            days = emptyList(),
        )

        val previewActivePlan = ActivePlan(planId = 1, currentDay = 22)

        PlansScreen(
            state = PlansState(
                isLoading = false,
                allPlans = listOf(previewPlan),
                activePlan = previewActivePlan,
                activePlanDetail = previewPlan,
            )
        )
    }
}

