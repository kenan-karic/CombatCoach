package io.aethibo.combatcoach.features.progress.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import io.aethibo.combatcoach.shared.log.domain.model.ExerciseLog

@Composable
internal fun ExerciseLogRow(exLog: ExerciseLog, modifier: Modifier = Modifier) {
    val sp = LocalSpacing.current

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(sp.medium),
            verticalArrangement = Arrangement.spacedBy(sp.xs),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = exLog.exerciseName,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = stringResource(R.string.exercise_sets_count, exLog.setsCompleted),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            // Per-set breakdown
            if (exLog.repsPerSet.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(sp.xs),
                ) {
                    exLog.repsPerSet.forEachIndexed { index, reps ->
                        val weight = exLog.weightPerSet.getOrElse(index) { 0f }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surface,
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text(
                                    text = stringResource(R.string.exercise_reps_suffix, reps),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary,
                                )
                                if (weight > 0f) {
                                    Text(
                                        text = stringResource(
                                            R.string.exercise_weight_suffix,
                                            weight
                                        ),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Exercise Log - Strength")
@Composable
private fun ExerciseLogRowStrengthPreview() {
    CombatCoachTheme {
        ExerciseLogRow(
            exLog = ExerciseLog(
                exerciseName = "Bench Press",
                setsCompleted = 3,
                repsPerSet = listOf(10, 8, 6),
                weightPerSet = listOf(80f, 85f, 90f)
            )
        )
    }
}

@Preview(showBackground = true, name = "Exercise Log - Bodyweight")
@Composable
private fun ExerciseLogRowBodyweightPreview() {
    CombatCoachTheme {
        ExerciseLogRow(
            exLog = ExerciseLog(
                exerciseName = "Pull Ups",
                setsCompleted = 4,
                repsPerSet = listOf(12, 10, 10, 8),
                weightPerSet = emptyList() // Weight > 0 check will hide the kg label
            )
        )
    }
}
