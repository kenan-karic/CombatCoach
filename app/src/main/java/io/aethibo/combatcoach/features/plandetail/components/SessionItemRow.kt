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

@Composable
internal fun SessionItemRow(
    name: String,
    discipline: Discipline,
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
                    text = name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = detail,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(Icons.Filled.PlayArrow, null, tint = accentColor, modifier = Modifier.size(18.dp))
        }
    }
}

@Preview(name = "Session Item List - Light", showBackground = true)
@Preview(
    name = "Session Item List - Dark",
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun SessionItemRowPreview() {
    CombatCoachTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Workout Type (Strength/Fitness)
            SessionItemRow(
                name = "Full Body Conditioning",
                discipline = Discipline.STRENGTH,
                detail = "45 mins • 8 exercises",
                isWorkout = true,
                onClick = {
                    // no-op
                }
            )

            // Combo Type (Muay Thai/Striking)
            SessionItemRow(
                name = "Lead Hook + Low Kick",
                discipline = Discipline.MUAY_THAI,
                detail = "5 rounds • 30s rest",
                isWorkout = false,
                onClick = {
                    // no-op
                }
            )

            // Boxing session
            SessionItemRow(
                name = "Boxing Footwork Basics",
                discipline = Discipline.BOXING,
                detail = "20 mins • 4 drills",
                isWorkout = true,
                onClick = {
                    // no-op
                }
            )
        }
    }
}
