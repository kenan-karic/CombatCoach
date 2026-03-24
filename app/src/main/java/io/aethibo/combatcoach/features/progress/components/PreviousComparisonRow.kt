package io.aethibo.combatcoach.features.progress.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingFlat
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
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing
import io.aethibo.combatcoach.shared.log.domain.model.WorkoutLog
import io.aethibo.combatcoach.shared.utils.toMinutesSeconds
import kotlin.math.abs

@Composable
fun PreviousComparisonRow(
    current: WorkoutLog,
    previous: WorkoutLog,
    modifier: Modifier = Modifier,
) {
    val sp = LocalSpacing.current
    val diff = current.durationSeconds - previous.durationSeconds
    val improved = diff < 0

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (improved) MaterialTheme.colorScheme.tertiaryContainer
        else MaterialTheme.colorScheme.surfaceVariant,
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(sp.medium),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(sp.small),
        ) {
            Icon(
                imageVector = if (improved) Icons.AutoMirrored.Filled.TrendingDown else Icons.AutoMirrored.Filled.TrendingFlat,
                contentDescription = null,
                tint = if (improved) MaterialTheme.colorScheme.tertiary
                else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp),
            )
            Column {
                Text(
                    text = if (improved) {
                        stringResource(R.string.comparison_faster)
                    } else {
                        stringResource(R.string.comparison_neutral)
                    },
                    style = MaterialTheme.typography.titleSmall,
                    color = if (improved) MaterialTheme.colorScheme.tertiary
                    else MaterialTheme.colorScheme.onSurface,
                )

                val diffSign = if (diff < 0) "-" else "+"

                val detailsText = stringResource(
                    R.string.comparison_details,
                    previous.durationSeconds.toMinutesSeconds(),
                    diffSign,
                    abs(diff).toMinutesSeconds()
                )
                Text(
                    text = detailsText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Comparison - Faster (Success)")
@Composable
private fun PreviousComparisonImprovedPreview() {
    CombatCoachTheme {
        PreviousComparisonRow(
            current = WorkoutLog(durationSeconds = 1140), // 19:00
            previous = WorkoutLog(durationSeconds = 1200) // 20:00
        )
    }
}

@Preview(showBackground = true, name = "Comparison - Slower")
@Composable
private fun PreviousComparisonSlowerPreview() {
    CombatCoachTheme {
        PreviousComparisonRow(
            current = WorkoutLog(durationSeconds = 1260), // 21:00
            previous = WorkoutLog(durationSeconds = 1200) // 20:00
        )
    }
}
