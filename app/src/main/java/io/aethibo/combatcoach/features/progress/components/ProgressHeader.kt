package io.aethibo.combatcoach.features.progress.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.R
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme
import io.aethibo.combatcoach.core.ui.theme.CoralPink
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing
import io.aethibo.combatcoach.features.progress.model.WeekDay
import io.aethibo.combatcoach.shared.log.domain.model.DashboardStats

@Composable
internal fun ProgressHeader(
    stats: DashboardStats,
    weekDays: List<WeekDay>,
    modifier: Modifier = Modifier,
) {
    val sp = LocalSpacing.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = sp.screenPadding)
            .padding(top = sp.xl, bottom = sp.large),
    ) {
        Text(
            text = stringResource(R.string.progress_title),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )

        Spacer(Modifier.height(sp.large))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            BigStatCard(
                label = stringResource(R.string.label_total_sessions),
                value = stats.totalWorkouts.toString(),
                icon = Icons.Filled.FitnessCenter,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f),
            )
            Spacer(Modifier.width(sp.small))
            BigStatCard(
                label = stringResource(R.string.label_current_streak),
                value = stringResource(R.string.streak_days_suffix, stats.currentStreak),
                icon = Icons.Filled.LocalFireDepartment,
                color = CoralPink,
                modifier = Modifier.weight(1f),
            )
            Spacer(Modifier.width(sp.small))
            BigStatCard(
                label = stringResource(R.string.label_total_minutes),
                value = stats.totalMinutes.toString(),
                icon = Icons.Filled.Timer,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(Modifier.height(sp.large))

        Text(
            text = stringResource(R.string.this_week_title),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(sp.small))
        WeeklyActivityChart(weekDays = weekDays)
    }
}

@Composable
private fun BigStatCard(
    label: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
) {
    val sp = LocalSpacing.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 96.dp)
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = Color.Black.copy(alpha = 0.05f),
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(sp.small),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(24.dp),
            )
            Spacer(modifier = Modifier.height(sp.xs))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                color = color,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(sp.xs))
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                maxLines = 2,
            )
        }
    }
}

@Composable
private fun WeeklyActivityChart(weekDays: List<WeekDay>) {
    val maxCount = weekDays.maxOf { it.sessionCount }.coerceAtLeast(1)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom,
    ) {
        weekDays.forEach { day ->
            val fraction = day.sessionCount.toFloat() / maxCount.toFloat()
            val animFraction by animateFloatAsState(
                targetValue = fraction,
                animationSpec = tween(600, easing = FastOutSlowInEasing),
                label = "barHeight${day.label}",
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
            ) {
                if (day.sessionCount > 0) {
                    Text(
                        text = day.sessionCount.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        color = if (day.isToday) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.height(2.dp))
                }

                val barColor = when {
                    day.isToday -> MaterialTheme.colorScheme.primary
                    day.sessionCount > 0 -> MaterialTheme.colorScheme.primaryContainer
                    else -> MaterialTheme.colorScheme.surfaceVariant
                }
                Box(
                    modifier = Modifier
                        .width(28.dp)
                        .height((48.dp * animFraction.coerceAtLeast(0.08f)))
                        .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                        .background(barColor)
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = day.label,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (day.isToday) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

private val previewWeek = listOf(
    WeekDay("M", 1, false),
    WeekDay("T", 2, false),
    WeekDay("W", 0, false),
    WeekDay("T", 3, false),
    WeekDay("F", 1, false),
    WeekDay("S", 0, false),
    WeekDay("S", 2, true),
)

private val previewStats = DashboardStats(
    totalWorkouts = 48,
    currentStreak = 6,
    totalMinutes = 1240
)

@Preview(showBackground = true)
@Composable
private fun ProgressHeaderPreview() {
    CombatCoachTheme {
        ProgressHeader(
            stats = previewStats,
            weekDays = previewWeek
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProgressHeaderEmptyPreview() {
    CombatCoachTheme {
        ProgressHeader(
            stats = DashboardStats(
                totalWorkouts = 0,
                currentStreak = 0,
                totalMinutes = 0
            ),
            weekDays = listOf(
                WeekDay("M", 0, false),
                WeekDay("T", 0, false),
                WeekDay("W", 0, false),
                WeekDay("T", 0, false),
                WeekDay("F", 0, false),
                WeekDay("S", 0, false),
                WeekDay("S", 0, true),
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProgressHeaderHighActivityPreview() {
    CombatCoachTheme {
        ProgressHeader(
            stats = DashboardStats(
                totalWorkouts = 120,
                currentStreak = 21,
                totalMinutes = 5400
            ),
            weekDays = listOf(
                WeekDay("M", 3, false),
                WeekDay("T", 4, false),
                WeekDay("W", 2, false),
                WeekDay("T", 5, false),
                WeekDay("F", 4, false),
                WeekDay("S", 6, false),
                WeekDay("S", 5, true),
            )
        )
    }
}
