package io.aethibo.combatcoach.features.create.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.R
import io.aethibo.combatcoach.core.ui.components.NumberField
import io.aethibo.combatcoach.core.ui.components.ccTextFieldColors
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing
import io.aethibo.combatcoach.shared.workout.domain.model.Exercise
import io.aethibo.combatcoach.shared.workout.domain.model.WorkoutType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExerciseSheet(
    onAdd: (Exercise) -> Unit,
    onDismiss: () -> Unit,
    workoutType: WorkoutType = WorkoutType.STRENGTH,
) {
    val sp = LocalSpacing.current
    var name by remember { mutableStateOf("") }
    var sets by remember { mutableIntStateOf(3) }
    var reps by remember { mutableIntStateOf(10) }
    var rest by remember { mutableIntStateOf(60) }
    // CIRCUIT is always time-based; STRENGTH is always rep-based.
    // The toggle is removed — type is derived from workoutType.
    val isTimed = workoutType == WorkoutType.CIRCUIT
    var duration by remember { mutableIntStateOf(30) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = sp.screenPadding)
                .padding(bottom = sp.xxxl),
            verticalArrangement = Arrangement.spacedBy(sp.small),
        ) {
            Text(
                text = when (workoutType) {
                    WorkoutType.CIRCUIT -> "Add Station"
                    WorkoutType.STRENGTH -> "Add Exercise"
                },
                style = MaterialTheme.typography.headlineSmall,
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = ccTextFieldColors(),
            )

            Row(horizontalArrangement = Arrangement.spacedBy(sp.small)) {
                NumberField("Sets", sets, { sets = it.coerceAtLeast(1) }, Modifier.weight(1f))
                if (isTimed) {
                    NumberField(
                        label = "Seconds",
                        value = duration,
                        onValue = { duration = it.coerceAtLeast(1) },
                        modifier = Modifier.weight(1f),
                    )
                } else {
                    // STRENGTH — reps only, no duration field
                    NumberField(
                        label = "Reps",
                        value = reps,
                        onValue = { reps = it.coerceAtLeast(1) },
                        modifier = Modifier.weight(1f),
                    )
                }
                NumberField("Rest (s)", rest, { rest = it.coerceAtLeast(0) }, Modifier.weight(1f))
            }

            Spacer(Modifier.height(sp.xs))

            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        onAdd(
                            Exercise(
                                name = name.trim(),
                                sets = sets,
                                reps = if (!isTimed) reps else null,
                                durationSeconds = if (isTimed) duration else null,
                                restSeconds = rest,
                            )
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(24.dp),
                enabled = name.isNotBlank(),
                elevation = ButtonDefaults.buttonElevation(0.dp),
            ) {
                Text(
                    text = when (workoutType) {
                        WorkoutType.CIRCUIT -> stringResource(R.string.workout_edit_action_add_station)
                        WorkoutType.STRENGTH -> stringResource(R.string.workout_edit_action_add_exercise)
                    },
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}
