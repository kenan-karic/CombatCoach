package io.aethibo.combatcoach.features.progress.components

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.SportsMartialArts
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.core.ui.components.CircleBadge
import io.aethibo.combatcoach.core.ui.components.DisciplineChip
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing
import io.aethibo.combatcoach.features.progress.model.LogEntryUi
import io.aethibo.combatcoach.shared.log.domain.model.WorkoutLog
import io.aethibo.combatcoach.shared.utils.Discipline
import io.aethibo.combatcoach.shared.utils.toMinutesSeconds
import io.aethibo.combatcoach.shared.utils.toRelativeDate
import io.aethibo.combatcoach.shared.workout.domain.model.WorkoutType
import kotlin.math.abs

@Composable
fun LogEntryCard(
    entry: LogEntryUi,
    onTap: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sp = LocalSpacing.current
    val accentColor = entry.discipline.accentColor()

    Card(
        onClick = onTap,
        modifier = modifier
            .fillMaxWidth()
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(sp.cardPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CircleBadge(
                size = 44.dp,
                backgroundColor = accentColor.copy(alpha = 0.12f),
            ) {
                Icon(
                    imageVector = if (entry.type == WorkoutType.STRENGTH)
                        Icons.Filled.FitnessCenter
                    else
                        Icons.Filled.SportsMartialArts,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(22.dp),
                )
            }

            Spacer(Modifier.width(sp.medium))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.workoutName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(sp.xxs))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(sp.xs),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    DisciplineChip(discipline = entry.discipline)
                    Text(
                        text = entry.log.completedAt.toRelativeDate(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            Spacer(Modifier.width(sp.small))

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = entry.log.durationSeconds.toMinutesSeconds(),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                entry.previousLog?.let { prev ->
                    val diff = entry.log.durationSeconds - prev.durationSeconds
                    val improved = diff < 0
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                    ) {
                        Icon(
                            // NOTE: TrendingDown = faster (less time) = good.
                            // TrendingUp = slower (more time) = worse.
                            // If your metric were e.g. weight lifted, this
                            // would be the opposite. Consider a custom icon
                            // or labelling the indicator "faster"/"slower"
                            // to avoid the ambiguity.
                            imageVector = if (improved) Icons.AutoMirrored.Filled.TrendingDown
                            else Icons.AutoMirrored.Filled.TrendingUp,
                            contentDescription = null,
                            tint = if (improved) MaterialTheme.colorScheme.tertiary
                            else MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(14.dp),
                        )
                        Text(
                            text = "${abs(diff)}s",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (improved) MaterialTheme.colorScheme.tertiary
                            else MaterialTheme.colorScheme.error,
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Log Entry - Improved")
@Composable
private fun LogEntryCardRealDataPreview() {
    CombatCoachTheme {
        val now = System.currentTimeMillis()

        // Mocking an entry from 2 hours ago where the user was 30s faster
        val entry = LogEntryUi(
            workoutName = "Heavy Bag Power",
            discipline = Discipline.BOXING,
            type = WorkoutType.STRENGTH,
            log = WorkoutLog(
                durationSeconds = 1200, // 20:00
                completedAt = now - (2 * 60 * 60 * 1000) // 2 hours ago
            ),
            previousLog = WorkoutLog(
                durationSeconds = 1230 // 20:30 (Slower than current)
            )
        )

        Column(Modifier.padding(16.dp)) {
            LogEntryCard(
                entry = entry,
                onTap = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "Log Entry - No Previous")
@Composable
private fun LogEntryCardNewPreview() {
    CombatCoachTheme {
        val entry = LogEntryUi(
            workoutName = "Leg Day A",
            discipline = Discipline.STRENGTH,
            type = WorkoutType.STRENGTH,
            log = WorkoutLog(
                durationSeconds = 3600,
                completedAt = System.currentTimeMillis()
            ),
            previousLog = null
        )

        LogEntryCard(entry = entry, onTap = {})
    }
}
