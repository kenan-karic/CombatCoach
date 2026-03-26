package io.aethibo.combatcoach.features.plandetail.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.R
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing
import io.aethibo.combatcoach.features.plandetail.PlanDetailState
import io.aethibo.combatcoach.shared.plan.domain.model.ActivePlan
import io.aethibo.combatcoach.shared.plan.domain.model.Plan

@Composable
internal fun PlanProgressCard(state: PlanDetailState, modifier: Modifier = Modifier) {
    val sp = LocalSpacing.current
    val animProgress by animateFloatAsState(
        targetValue = state.progressFraction,
        animationSpec = tween(600, easing = FastOutSlowInEasing),
        label = "planProgress",
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.05f),
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(sp.cardPadding),
            verticalArrangement = Arrangement.spacedBy(sp.small)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.plan_detail_progress_title),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    state.currentDay?.let { day ->
                        Text(
                            text = stringResource(
                                R.string.plan_detail_progress_next_label,
                                day.label
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Text(
                    text = stringResource(
                        R.string.plan_detail_progress_fraction_format,
                        state.activePlan?.currentDay ?: 1,
                        state.plan?.totalDays ?: 0
                    ),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            LinearProgressIndicator(
                progress = { animProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primaryContainer,
                strokeCap = StrokeCap.Round,
            )

            Text(
                text = stringResource(
                    R.string.plan_detail_progress_percent_format,
                    state.progressPercent
                ),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(name = "Plan Progress - Light", showBackground = true)
@Preview(
    name = "Plan Progress - Dark",
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PlanProgressCardPreview() {
    CombatCoachTheme {
        val mockState = PlanDetailState(
            plan = Plan(name = "Muay Thai 8 week"),
            activePlan = ActivePlan(planId = 1, currentDay = 12),
            isActivePlan = true,
            eventSink = {
                // no-op
            }
        )

        PlanProgressCard(state = mockState)
    }
}
