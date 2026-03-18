package io.aethibo.combatcoach.features.plan.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.core.ui.components.DisciplineChip
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing
import io.aethibo.combatcoach.shared.plan.domain.model.ActivePlan
import io.aethibo.combatcoach.shared.plan.domain.model.Plan
import io.aethibo.combatcoach.shared.plan.domain.model.PlanType
import io.aethibo.combatcoach.shared.utils.Discipline

private fun buildPlanMeta(plan: Plan): String {
    val parts = mutableListOf<String>()
    parts += "${plan.totalDays} ${if (plan.totalDays == 1) "day" else "days"}"
    if (plan.planType == PlanType.PROGRAM && plan.totalDays > 7) {
        parts += "${(plan.totalDays + 6) / 7} weeks"
    }
    val restDays = plan.days.count { it.isRestDay }
    if (restDays > 0) parts += "$restDays rest"
    return parts.joinToString(" · ")
}

@Composable
internal fun PlanCard(
    plan: Plan,
    isActive: Boolean,
    activePlan: ActivePlan?,
    onClick: () -> Unit,
) {
    val sp = LocalSpacing.current
    val accentColor = plan.discipline.accentColor()

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.05f),
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = if (isActive) BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary) else null,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(sp.cardPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(accentColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (plan.planType) {
                        PlanType.PROGRAM -> Icons.Filled.CalendarMonth
                        PlanType.COLLECTION -> Icons.Filled.FolderOpen
                    },
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(24.dp),
                )
            }

            Spacer(Modifier.width(sp.medium))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    plan.name, style = MaterialTheme.typography.titleMedium,
                    maxLines = 1, overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(sp.xxs))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(sp.xs),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DisciplineChip(discipline = plan.discipline)
                    Text(
                        buildPlanMeta(plan), style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (isActive && activePlan != null && plan.totalDays > 0) {
                    Spacer(Modifier.height(sp.xs))
                    val progress = (activePlan.currentDay - 1).toFloat() / plan.totalDays.toFloat()
                    val animProg by animateFloatAsState(
                        targetValue = progress,
                        animationSpec = tween(500),
                        label = "cardProgress",
                    )
                    LinearProgressIndicator(
                        progress = { animProg },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.primaryContainer,
                        strokeCap = StrokeCap.Round,
                    )
                }
            }

            Spacer(Modifier.width(sp.small))
            Icon(
                Icons.Filled.ChevronRight, null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview(showBackground = true, name = "PlanCard — active")
@Composable
private fun PlanCardActivePreview() {
    CombatCoachTheme {
        val previewPlan = Plan(
            id = 1,
            name = "8-Week Boxing Program",
            planType = PlanType.PROGRAM,
            discipline = Discipline.STRIKING,
            days = emptyList(),
        )

        val previewActivePlan = ActivePlan(planId = 1, currentDay = 22)

        PlanCard(
            plan = previewPlan,
            isActive = true,
            activePlan = previewActivePlan,
            onClick = {
                // no-op
            },
        )
    }
}

@Preview(showBackground = true, name = "PlanCard — inactive")
@Composable
private fun PlanCardInactivePreview() {
    CombatCoachTheme {
        val previewPlan = Plan(
            id = 1,
            name = "8-Week Boxing Program",
            planType = PlanType.PROGRAM,
            discipline = Discipline.STRIKING,
            days = emptyList(),
        )

        PlanCard(
            plan = previewPlan,
            isActive = false,
            activePlan = null,
            onClick = {
                // no-op
            },
        )
    }
}
