package io.aethibo.combatcoach.features.dashboard.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.R
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing
import io.aethibo.combatcoach.shared.plan.domain.model.ActivePlan
import io.aethibo.combatcoach.shared.plan.domain.model.Plan
import io.aethibo.combatcoach.shared.plan.domain.utils.PlanProgress

@Composable
internal fun ActivePlanCard(
    progress: PlanProgress,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sp = LocalSpacing.current

    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.05f)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(sp.cardPadding)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        progress.plan.name,
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(sp.xxs))
                    Text(
                        progress.currentDayLabel,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                CircularPlanProgress(
                    fraction = progress.progressFraction,
                    label = stringResource(
                        R.string.plan_progress_fraction_format,
                        progress.completedDays,
                        progress.totalDays
                    ),
                )
            }
            Spacer(Modifier.height(sp.medium))
            LinearProgressIndicator(
                progress = { progress.progressFraction },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primaryContainer,
                strokeCap = StrokeCap.Round,
            )
            Spacer(Modifier.height(sp.xs))
            Text(
                text = stringResource(
                    R.string.plan_percent_complete_format,
                    (progress.progressFraction * 100).toInt()
                ),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun CircularPlanProgress(fraction: Float, label: String) {
    val animatedFraction by animateFloatAsState(
        targetValue = fraction,
        animationSpec = tween(800, easing = FastOutSlowInEasing),
        label = "planProgress",
    )
    val gradientStart = MaterialTheme.colorScheme.primary
    val gradientEnd = MaterialTheme.colorScheme.tertiary

    Box(modifier = Modifier.size(56.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 5.dp.toPx()
            // Track
            drawArc(
                color = Color.Black.copy(alpha = 0.08f), startAngle = -90f, sweepAngle = 360f,
                useCenter = false, style = Stroke(strokeWidth, cap = StrokeCap.Round)
            )
            // Progress
            drawArc(
                brush = Brush.linearGradient(
                    listOf(gradientStart, gradientEnd),
                    start = Offset(0f, 0f), end = Offset(size.width, size.height)
                ),
                startAngle = -90f, sweepAngle = 360f * animatedFraction,
                useCenter = false, style = Stroke(strokeWidth, cap = StrokeCap.Round),
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(showBackground = true, name = "ActivePlanCard")
@Composable
private fun ActivePlanCardPreview() {
    CombatCoachTheme {
        val previewPlan = Plan(
            id = 1,
            name = "8-Week Boxing Program",
        )

        val previewPlanProgress = PlanProgress(
            plan = previewPlan,
            activePlan = ActivePlan(planId = 1, currentDay = 15),
            completedDays = 14,
            totalDays = 56,
        )

        ActivePlanCard(
            progress = previewPlanProgress,
            onClick = {
                // no-op
            },
        )
    }
}
