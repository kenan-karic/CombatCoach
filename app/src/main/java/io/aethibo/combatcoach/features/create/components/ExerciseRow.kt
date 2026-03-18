package io.aethibo.combatcoach.features.create.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.R
import io.aethibo.combatcoach.core.ui.components.NumberField
import io.aethibo.combatcoach.core.ui.components.ccTextFieldColors
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing
import io.aethibo.combatcoach.shared.workout.domain.model.Exercise
import io.aethibo.combatcoach.shared.workout.domain.model.WorkoutType

@Composable
fun ExerciseRow(
    exercise: Exercise,
    index: Int,
    total: Int,
    workoutType: WorkoutType,
    onUpdate: (Exercise) -> Unit,
    onRemove: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sp = LocalSpacing.current
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(12.dp),
                spotColor = Color.Black.copy(alpha = 0.05f),
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            // ── Header row ─────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(sp.medium),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Up / Down reorder — no library needed, just two icon buttons.
                // The buttons are disabled at the list boundaries so they never
                // produce out-of-bounds indices in the presenter.
                Column {
                    IconButton(
                        onClick = onMoveUp,
                        enabled = index > 0,
                        modifier = Modifier.size(32.dp),
                    ) {
                        Icon(
                            Icons.Filled.ArrowUpward,
                            contentDescription = "Move up",
                            modifier = Modifier.size(16.dp),
                            tint = if (index > 0)
                                MaterialTheme.colorScheme.onSurfaceVariant
                            else
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                        )
                    }
                    IconButton(
                        onClick = onMoveDown,
                        enabled = index < total - 1,
                        modifier = Modifier.size(24.dp),
                    ) {
                        Icon(
                            Icons.Filled.ArrowDownward,
                            contentDescription = "Move down",
                            modifier = Modifier.size(16.dp),
                            tint = if (index < total - 1)
                                MaterialTheme.colorScheme.onSurfaceVariant
                            else
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                        )
                    }
                }

                Spacer(Modifier.width(sp.xs))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = exercise.name.ifBlank { "Unnamed exercise" },
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = buildExerciseSummary(exercise, workoutType),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                IconButton(onClick = onRemove) {
                    Icon(
                        Icons.Filled.Close,
                        "Remove",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp),
                    )
                }
                Icon(
                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp),
                )
            }

            // ── Expanded editor ────────────────────────────────────────────
            AnimatedVisibility(visible = expanded) {
                ExerciseEditor(
                    exercise = exercise,
                    workoutType = workoutType,
                    onUpdate = onUpdate,
                    modifier = Modifier.padding(
                        start = sp.medium,
                        end = sp.medium,
                        bottom = sp.medium,
                    ),
                )
            }
        }
    }
}

// ── Inline editor (expanded state) ────────────────────────────────────────────

@Composable
private fun ExerciseEditor(
    exercise: Exercise,
    workoutType: WorkoutType,
    onUpdate: (Exercise) -> Unit,
    modifier: Modifier = Modifier,
) {
    val sp = LocalSpacing.current
    // Timed = CIRCUIT. STRENGTH is always rep-based — the toggle is gone.
    val isTimed = workoutType == WorkoutType.CIRCUIT

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(sp.small),
    ) {
        OutlinedTextField(
            value = exercise.name,
            onValueChange = { onUpdate(exercise.copy(name = it)) },
            label = { Text(text = stringResource(R.string.exercise_row_label_name)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            colors = ccTextFieldColors(),
        )

        Row(horizontalArrangement = Arrangement.spacedBy(sp.small)) {
            NumberField(
                label = stringResource(R.string.exercise_field_sets),
                value = exercise.sets,
                onValue = { onUpdate(exercise.copy(sets = it.coerceAtLeast(1))) },
                modifier = Modifier.weight(1f),
            )
            if (isTimed) {
                NumberField(
                    label = stringResource(R.string.exercise_field_seconds),
                    value = exercise.durationSeconds ?: 30,
                    onValue = { onUpdate(exercise.copy(durationSeconds = it.coerceAtLeast(1))) },
                    modifier = Modifier.weight(1f),
                )
            } else {
                NumberField(
                    label = stringResource(R.string.exercise_field_reps),
                    value = exercise.reps ?: 10,
                    onValue = { onUpdate(exercise.copy(reps = it.coerceAtLeast(1))) },
                    modifier = Modifier.weight(1f),
                )
            }
            NumberField(
                label = stringResource(R.string.exercise_field_rest),
                value = exercise.restSeconds,
                onValue = { onUpdate(exercise.copy(restSeconds = it.coerceAtLeast(0))) },
                modifier = Modifier.weight(1f),
            )
        }

        OutlinedTextField(
            value = exercise.notes,
            onValueChange = { onUpdate(exercise.copy(notes = it)) },
            label = { Text("Notes (optional)") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            maxLines = 2,
            colors = ccTextFieldColors(),
        )
    }
}

// ── Summary line ───────────────────────────────────────────────────────────────

@Composable
private fun buildExerciseSummary(exercise: Exercise, workoutType: WorkoutType): String {
    val parts = mutableListOf<String>()
    parts += stringResource(R.string.exercise_summary_sets, exercise.sets)

    when (workoutType) {
        WorkoutType.CIRCUIT -> exercise.durationSeconds?.let {
            parts += stringResource(R.string.exercise_summary_seconds, it)
        }

        WorkoutType.STRENGTH -> exercise.reps?.let {
            parts += stringResource(R.string.exercise_summary_reps, it)
        }
    }

    if (exercise.restSeconds > 0) {
        parts += stringResource(R.string.exercise_summary_rest, exercise.restSeconds)
    }

    return parts.joinToString(stringResource(R.string.exercise_summary_separator))
}

@Preview(name = "Exercise Row States - Light", showBackground = true)
@Composable
private fun ExerciseRowPreview() {
    CombatCoachTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Collapsed Circuit Station (Position 1 of 3)
            ExerciseRow(
                exercise = Exercise(
                    name = "Roundhouse Kicks",
                    sets = 3,
                    durationSeconds = 45,
                    restSeconds = 15
                ),
                index = 0,
                total = 3,
                workoutType = WorkoutType.CIRCUIT,
                onUpdate = {
                    // no-op
                },
                onRemove = {
                    // no-op
                },
                onMoveUp = {
                    // no-op
                },
                onMoveDown = {
                    // no-op
                }
            )

            ExerciseRow(
                exercise = Exercise(
                    name = "Bench Press",
                    sets = 4,
                    reps = 10,
                    restSeconds = 90,
                    notes = "Focus on explosive upward movement"
                ),
                index = 1,
                total = 3,
                workoutType = WorkoutType.STRENGTH,
                onUpdate = {
                    // no-op
                },
                onRemove = {
                    // no-op
                },
                onMoveUp = {
                    // no-op
                },
                onMoveDown = {
                    // no-op
                }
            )
        }
    }
}
