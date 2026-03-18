package io.aethibo.combatcoach.features.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.R
import io.aethibo.combatcoach.core.ui.components.StatItem
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme
import io.aethibo.combatcoach.shared.log.domain.model.DashboardStats

@Composable
internal fun StatsStrip(
    stats: DashboardStats,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = Color.Black.copy(alpha = 0.05f),
            ),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            StatItem(
                label = stringResource(R.string.dashboard_stats_total),
                value = stats.totalWorkouts.toString(),
                valueColor = MaterialTheme.colorScheme.primary
            )

            VerticalDivider(Modifier.height(36.dp))

            StatItem(
                label = stringResource(R.string.dashboard_stats_this_week),
                value = stats.workoutsThisWeek.toString(),
                valueColor = MaterialTheme.colorScheme.secondary
            )

            VerticalDivider(Modifier.height(36.dp))

            StatItem(
                label = stringResource(R.string.dashboard_stats_streak),
                value = stringResource(R.string.stats_streak_days_format, stats.currentStreak),
                valueColor = MaterialTheme.colorScheme.tertiary
            )

            VerticalDivider(Modifier.height(36.dp))

            StatItem(
                label = stringResource(R.string.dashboard_stats_minutes),
                value = stats.totalMinutes.toString(),
                valueColor = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true, name = "Home stats strip")
@Composable
private fun StatsStripPreview() {
    CombatCoachTheme {
        StatsStrip(
            stats = DashboardStats(
                totalWorkouts = 15,
                workoutsThisWeek = 24,
                currentStreak = 5,
                totalMinutes = 180
            )
        )
    }
}
