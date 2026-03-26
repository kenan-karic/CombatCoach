package io.aethibo.combatcoach.features.timer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.shared.workout.domain.model.Exercise

@Composable
fun ExerciseProgressList(
    exercises: List<Exercise>,
    currentIndex: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        exercises.forEachIndexed { index, exercise ->
            val isDone = index < currentIndex
            val isCurrent = index == currentIndex
            val color = when {
                isDone -> MaterialTheme.colorScheme.tertiary
                isCurrent -> MaterialTheme.colorScheme.primary
                else -> MaterialTheme.colorScheme.outline
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(if (isCurrent) 10.dp else 8.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                if (isCurrent) {
                    Text(
                        text = exercise.name,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                    )
                }
            }
        }
    }
}
