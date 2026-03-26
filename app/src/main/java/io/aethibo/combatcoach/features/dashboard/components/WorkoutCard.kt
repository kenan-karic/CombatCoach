package io.aethibo.combatcoach.features.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.R
import io.aethibo.combatcoach.core.ui.components.CircleBadge
import io.aethibo.combatcoach.core.ui.components.DisciplineChip
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing
import io.aethibo.combatcoach.shared.utils.WorkoutDiscipline
import io.aethibo.combatcoach.shared.workout.domain.model.Exercise
import io.aethibo.combatcoach.shared.workout.domain.model.Workout
import io.aethibo.combatcoach.shared.workout.domain.model.WorkoutType

@Composable
fun WorkoutCard(
    workout: Workout,
    onStart: () -> Unit,
    onOpen: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sp = LocalSpacing.current
    val exerciseText = stringResource(R.string.workout_exercise_count, workout.exerciseCount)
    val durationText = if (workout.estimatedDurationMinutes > 0) {
        stringResource(R.string.workout_duration_minutes_format, workout.estimatedDurationMinutes)
    } else ""

    Card(
        onClick = onOpen,
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(sp.cardPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CircleBadge(
                size = 48.dp,
                backgroundColor = workout.workoutDiscipline.accentColor().copy(alpha = 0.12f),
            ) {
                Icon(
                    imageVector = workout.type.icon(),
                    contentDescription = stringResource(R.string.cd_start_workout, workout.name),
                    tint = workout.workoutDiscipline.accentColor(),
                    modifier = Modifier.size(24.dp),
                )
            }

            Spacer(Modifier.width(sp.medium))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = workout.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(sp.xxs))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    DisciplineChip(discipline = workout.workoutDiscipline)
                    Spacer(Modifier.width(sp.small))
                    Text(
                        text = exerciseText + durationText,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        modifier = Modifier
                            .weight(1f)
                            .basicMarquee(),
                    )
                }
            }

            Spacer(Modifier.width(sp.small))

            IconButton(
                onClick = onStart,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = "Start ${workout.name}",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(20.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "WorkoutCard")
@Composable
private fun WorkoutCardPreview() {
    CombatCoachTheme {
        val sampleExercises = listOf(
            Exercise(
                name = "Push-ups",
                sets = 4,
                reps = 15,
                durationSeconds = null,
                restSeconds = 60,
                notes = "Keep core tight",
                orderIndex = 0
            ),
            Exercise(
                name = "Plank Hold",
                sets = 3,
                reps = null,
                durationSeconds = 45,
                restSeconds = 45,
                notes = "Maintain neutral spine",
                orderIndex = 1
            ),
            Exercise(
                name = "Jump Squats",
                sets = 3,
                reps = 12,
                durationSeconds = null,
                restSeconds = 60,
                notes = "Explosive movement",
                orderIndex = 2
            ),
            Exercise(
                name = "Shadow Boxing",
                sets = 5,
                reps = null,
                durationSeconds = 120,
                restSeconds = 30,
                notes = "Focus on speed and combos",
                orderIndex = 3
            ),
            Exercise(
                name = "Pull-ups",
                sets = 4,
                reps = 8,
                durationSeconds = null,
                restSeconds = 90,
                notes = "Full range of motion",
                orderIndex = 4
            ),
            Exercise(
                name = "Wall Sit",
                sets = 3,
                reps = null,
                durationSeconds = 60,
                restSeconds = 45,
                notes = "Thighs parallel to ground",
                orderIndex = 5
            ),
            Exercise(
                name = "Burpees",
                sets = 4,
                reps = 10,
                durationSeconds = null,
                restSeconds = 75,
                notes = "Steady pace",
                orderIndex = 6
            ),
            Exercise(
                name = "Heavy Bag Rounds",
                sets = 6,
                reps = null,
                durationSeconds = 180,
                restSeconds = 60,
                notes = "Power combinations",
                orderIndex = 7
            ),
            Exercise(
                name = "Lunges",
                sets = 3,
                reps = 12,
                durationSeconds = null,
                restSeconds = 60,
                notes = "12 reps per leg",
                orderIndex = 8
            ),
            Exercise(
                name = "Mountain Climbers",
                sets = 4,
                reps = null,
                durationSeconds = 40,
                restSeconds = 40,
                notes = "Fast but controlled",
                orderIndex = 9
            )
        )

        val previewWorkout = Workout(
            id = 1,
            name = "Boxing Fundamentals",
            workoutDiscipline = WorkoutDiscipline.CIRCUIT,
            type = WorkoutType.CIRCUIT,
            estimatedDurationMinutes = 45,
            exercises = sampleExercises
        )

        WorkoutCard(
            workout = previewWorkout,
            onStart = {
                // no-op
            },
            onOpen = {
                // no-op
            },
        )
    }
}
