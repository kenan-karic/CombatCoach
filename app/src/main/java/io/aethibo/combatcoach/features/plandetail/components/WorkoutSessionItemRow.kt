package io.aethibo.combatcoach.features.plandetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SportsMartialArts
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing
import io.aethibo.combatcoach.shared.utils.Discipline
import io.aethibo.combatcoach.shared.utils.TrainingDiscipline

@Composable
internal fun WorkoutSessionItemRow(
    name: String,
    discipline: TrainingDiscipline,
    detail: String,
    isWorkout: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sp = LocalSpacing.current
    val accentColor = discipline.accentColor()

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = sp.small, vertical = sp.xs),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(sp.small),
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    if (isWorkout) Icons.Filled.FitnessCenter else Icons.Filled.SportsMartialArts,
                    null,
                    tint = accentColor,
                    modifier = Modifier.size(16.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    detail,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(Icons.Filled.PlayArrow, null, tint = accentColor, modifier = Modifier.size(18.dp))
        }
    }
}

@Preview(name = "Workout Sessions - Light", showBackground = true)
@Preview(
    name = "Workout Sessions - Dark",
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun WorkoutSessionItemRowPreview() {
    CombatCoachTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Workout Variation (using Strength/Fitness style)
            WorkoutSessionItemRow(
                name = "Explosive Power Circuit",
                discipline = Discipline.STRENGTH,
                detail = "45 mins • 12 exercises",
                isWorkout = true,
                onClick = {
                    // no-op
                }
            )

            // Technique Variation (using Muay Thai style)
            WorkoutSessionItemRow(
                name = "Clinch Knees & Elbows",
                discipline = Discipline.MUAY_THAI,
                detail = "5 Rounds • Technical",
                isWorkout = false,
                onClick = {
                    // no-op
                }
            )

            // Boxing Variation
            WorkoutSessionItemRow(
                name = "Heavy Bag: Speed Focus",
                discipline = Discipline.BOXING,
                detail = "30 mins • 6 exercises",
                isWorkout = true,
                onClick = {
                    // no-op
                }
            )
        }
    }
}
