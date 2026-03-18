package io.aethibo.combatcoach.features.plandetail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing
import io.aethibo.combatcoach.shared.workout.domain.model.Workout

@Composable
internal fun CollectionView(
    workoutIds: List<Int>,
    workouts: Map<Int, Workout>,
    currentIdx: Int,
    isActive: Boolean,
    onStart: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val sp = LocalSpacing.current

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(sp.cardGap),
    ) {
        workoutIds.forEachIndexed { index, id ->
            val workout = workouts[id] ?: return@forEachIndexed
            val isCurrent = isActive && index == currentIdx
            val isDone = isActive && index < currentIdx

            Card(
                onClick = { onStart(id) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
                border = if (isCurrent) BorderStroke(
                    1.5.dp,
                    MaterialTheme.colorScheme.primary
                ) else null,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(sp.medium),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    DayBadge(dayIndex = index, isDone = isDone, isCurrent = isCurrent)
                    Spacer(Modifier.width(sp.medium))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            workout.name, style = MaterialTheme.typography.titleSmall, maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            "${workout.exerciseCount} exercises · ${workout.estimatedDurationMinutes}m",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    when {
                        isCurrent -> Icon(
                            Icons.Filled.PlayArrow,
                            null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(22.dp)
                        )

                        isDone -> Icon(
                            Icons.Filled.CheckCircle,
                            null,
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "Collection View - Light Mode", showBackground = true)
@Preview(
    name = "Collection View - Dark Mode",
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun CollectionViewPreview() {
    CombatCoachTheme {
        // Mocking the workouts map
        val mockWorkouts = mapOf(
            1 to Workout(
                id = 1,
                name = "Heavy Bag Basics",
                estimatedDurationMinutes = 20
            ),
            2 to Workout(
                id = 2,
                name = "Lead Hook Masterclass",
                estimatedDurationMinutes = 15
            ),
            3 to Workout(
                id = 3,
                name = "Clinch Conditioning",
                estimatedDurationMinutes = 30
            ),
            4 to Workout(
                id = 4,
                name = "Core & Stability",
                estimatedDurationMinutes = 10
            )
        )
        val workoutIds = listOf(1, 2, 3, 4)

        CollectionView(
            workoutIds = workoutIds,
            workouts = mockWorkouts,
            currentIdx = 1, // "Lead Hook Masterclass" is the active one
            isActive = true,
            onStart = {
                // no-op
            }
        )
    }
}
