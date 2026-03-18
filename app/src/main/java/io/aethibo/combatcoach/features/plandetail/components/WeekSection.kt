package io.aethibo.combatcoach.features.plandetail.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.R
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing
import io.aethibo.combatcoach.features.plandetail.model.WeekGroup
import io.aethibo.combatcoach.shared.combo.domain.model.Combo
import io.aethibo.combatcoach.shared.plan.domain.model.PlanDay
import io.aethibo.combatcoach.shared.workout.domain.model.Workout

@Composable
fun WeekSection(
    group: WeekGroup,
    currentDayIdx: Int,
    isActivePlan: Boolean,
    workouts: Map<Int, Workout>,
    combos: Map<Int, Combo>,
    onToggle: () -> Unit,
    onOpenSession: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val sp = LocalSpacing.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = if (group.isCurrentWeek) 2.dp else 1.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = Color.Black.copy(alpha = 0.05f),
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = if (group.isCurrentWeek)
            BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary) else null,
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggle() }
                    .padding(sp.medium),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                WeekProgressRing(
                    completed = group.completedCount,
                    total = group.days.size,
                    isCurrent = group.isCurrentWeek,
                )
                Spacer(Modifier.width(sp.medium))
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(sp.xs),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = group.label,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (group.isCurrentWeek) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.primaryContainer
                            ) {
                                Text(
                                    text = stringResource(R.string.plan_detail_week_current_label),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                )
                            }
                        }
                    }
                    Text(
                        text = stringResource(
                            R.string.plan_detail_week_days_complete_format,
                            group.completedCount,
                            group.days.size
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Icon(
                    imageVector = if (group.isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = stringResource(
                        if (group.isExpanded) R.string.cd_collapse_week else R.string.cd_expand_week
                    ),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp),
                )
            }

            AnimatedVisibility(
                visible = group.isExpanded,
                enter = expandVertically(tween(250)) + fadeIn(tween(250)),
                exit = shrinkVertically(tween(200)) + fadeOut(tween(200)),
            ) {
                Column(
                    modifier = Modifier.padding(
                        start = sp.medium,
                        end = sp.medium,
                        bottom = sp.medium
                    ),
                    verticalArrangement = Arrangement.spacedBy(sp.xs),
                ) {
                    group.days.forEach { day ->
                        val dayIndex = day.dayNumber - 1
                        DayCard(
                            day = day,
                            dayIndex = dayIndex,
                            currentDayIdx = currentDayIdx,
                            isActivePlan = isActivePlan,
                            workouts = workouts,
                            combos = combos,
                            onOpenSession = { onOpenSession(dayIndex) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WeekProgressRing(completed: Int, total: Int, isCurrent: Boolean) {
    val fraction = if (total == 0) 0f else completed.toFloat() / total.toFloat()
    val animFrac by animateFloatAsState(
        targetValue = fraction,
        animationSpec = tween(500),
        label = "weekRing"
    )
    val color =
        if (isCurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary

    Box(modifier = Modifier.size(40.dp), contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val stroke = 4.dp.toPx()
            drawArc(
                color = color.copy(alpha = 0.15f),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(
                    stroke,
                    cap = StrokeCap.Round
                )
            )
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = 360f * animFrac,
                useCenter = false,
                style = Stroke(stroke, cap = StrokeCap.Round)
            )
        }
        Text("$completed/$total", style = MaterialTheme.typography.labelSmall, color = color)
    }
}

@Preview(name = "Week Section List", showBackground = true)
@Composable
private fun WeekSectionPreview() {
    CombatCoachTheme {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                // Expanded Current Week
                WeekSection(
                    group = WeekGroup(
                        label = "Week 1",
                        days = List(7) { PlanDay(dayNumber = it + 1) },
                        completedCount = 3,
                        isCurrentWeek = true,
                        isExpanded = true,
                        weekNumber = 1
                    ),
                    currentDayIdx = 3,
                    isActivePlan = true,
                    workouts = emptyMap(),
                    combos = emptyMap(),
                    onToggle = {
                        // no-op
                    },
                    onOpenSession = {
                        // no-op
                    }
                )
            }
            item {
                // Collapsed Past Week
                WeekSection(
                    group = WeekGroup(
                        label = "Week 2",
                        days = List(7) { PlanDay(dayNumber = it + 8) },
                        completedCount = 7,
                        isCurrentWeek = false,
                        isExpanded = false,
                        weekNumber = 2
                    ),
                    currentDayIdx = 14,
                    isActivePlan = true,
                    workouts = emptyMap(),
                    combos = emptyMap(),
                    onToggle = {
                        // no-op
                    },
                    onOpenSession = {
                        // no-op
                    }
                )
            }
        }
    }
}
