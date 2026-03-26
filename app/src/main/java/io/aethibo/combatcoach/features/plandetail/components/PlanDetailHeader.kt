package io.aethibo.combatcoach.features.plandetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.R
import io.aethibo.combatcoach.core.ui.components.DisciplineChip
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing
import io.aethibo.combatcoach.features.plandetail.PlanDetailState
import io.aethibo.combatcoach.shared.plan.domain.model.Plan
import io.aethibo.combatcoach.shared.plan.domain.model.PlanDay
import io.aethibo.combatcoach.shared.plan.domain.model.PlanType
import io.aethibo.combatcoach.shared.utils.Discipline

@Composable
internal fun PlanDetailHeader(plan: Plan, state: PlanDetailState, modifier: Modifier = Modifier) {
    val sp = LocalSpacing.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = sp.screenPadding)
            .padding(top = sp.medium, bottom = sp.large),
        verticalArrangement = Arrangement.spacedBy(sp.small),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(sp.xs),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DisciplineChip(discipline = plan.discipline)
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Text(
                    text = plan.planType.label(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }
            if (state.isActivePlan) {
                AssistChip(
                    onClick = { /* No-op or same as parent */ },
                    label = {
                        Text(
                            text = stringResource(R.string.plan_detail_header_active_badge),
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Filled.PlayCircle,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp)
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        labelColor = MaterialTheme.colorScheme.tertiary,
                        leadingIconContentColor = MaterialTheme.colorScheme.tertiary
                    ),
                    border = null,
                    shape = RoundedCornerShape(20.dp)
                )
            }
        }

        if (plan.description.isNotBlank()) {
            Text(
                text = plan.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Quick stats
        Row(horizontalArrangement = Arrangement.spacedBy(sp.xl)) {
            QuickStat(
                label = stringResource(R.string.plan_detail_header_stat_days),
                value = plan.totalDays.toString()
            )
            if (plan.planType == PlanType.PROGRAM && plan.totalDays > 7) {
                QuickStat(
                    label = stringResource(R.string.plan_detail_header_stat_weeks),
                    value = ((plan.totalDays + 6) / 7).toString()
                )
            }
            val restDays = plan.days.count { it.isRestDay }
            if (restDays > 0) QuickStat(
                label = stringResource(R.string.plan_detail_header_stat_rest_days),
                value = restDays.toString()
            )
        }
    }
}

@Composable
private fun QuickStat(label: String, value: String) {
    Column {
        Text(
            value, style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            label, style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(name = "Plan Header - Inactive", showBackground = true)
@Composable
private fun PlanDetailHeaderInactivePreview() {
    CombatCoachTheme {
        val mockPlan = Plan(
            name = "Muay Thai Power",
            description = "A comprehensive 4-week program focusing on explosive striking and clinch work.",
            discipline = Discipline.MUAY_THAI,
            planType = PlanType.PROGRAM,
            days = List(28) { index ->
                PlanDay(
                    isRestDay = (index + 1) % 7 == 0,
                    dayNumber = 1,
                )
            }
        )

        PlanDetailHeader(
            plan = mockPlan,
            state = PlanDetailState(isActivePlan = false, eventSink = {})
        )
    }
}

@Preview(name = "Plan Header - Active", showBackground = true)
@Composable
private fun PlanDetailHeaderActivePreview() {
    CombatCoachTheme {
        val mockPlan = Plan(
            name = "Boxing Basics",
            description = "Master the fundamentals of footwork and the jab.",
            discipline = Discipline.BOXING,
            planType = PlanType.PROGRAM,
            days = listOf(PlanDay(dayNumber = 3, isRestDay = true))
        )

        PlanDetailHeader(
            plan = mockPlan,
            state = PlanDetailState(isActivePlan = true, eventSink = {})
        )
    }
}
