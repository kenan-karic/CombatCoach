package io.aethibo.combatcoach.features.create.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.core.ui.components.DisciplineChip
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme
import io.aethibo.combatcoach.shared.utils.WorkoutDiscipline
import io.aethibo.combatcoach.shared.workout.domain.model.Workout

@Composable
fun CollectionWorkoutPicker(
    selectedIds: List<Int>,
    availableWorkouts: List<Workout>,
    onAdd: (Int) -> Unit,
    onRemove: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        availableWorkouts.forEach { workout ->
            val selected = workout.id in selectedIds
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { if (selected) onRemove(workout.id) else onAdd(workout.id) }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    checked = selected,
                    onCheckedChange = { if (it) onAdd(workout.id) else onRemove(workout.id) },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary,
                    ),
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    workout.name,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                DisciplineChip(discipline = workout.workoutDiscipline)
            }
        }
    }
}

@Preview(name = "Workout Picker - Light", showBackground = true)
@Preview(
    name = "Workout Picker - Dark",
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun CollectionWorkoutPickerPreview() {
    CombatCoachTheme {
        val mockWorkouts = listOf(
            Workout(
                id = 1,
                name = "Heavy Bag Blast",
                workoutDiscipline = WorkoutDiscipline.BOXING
            ),
            Workout(
                id = 2,
                name = "Leg Day Strength",
                workoutDiscipline = WorkoutDiscipline.STRENGTH
            ),
            Workout(
                id = 3,
                name = "Muay Thai Clinch",
                workoutDiscipline = WorkoutDiscipline.MMA_CONDITIONING
            )
        )

        CollectionWorkoutPicker(
            selectedIds = listOf(1, 3),
            availableWorkouts = mockWorkouts,
            onAdd = {
                // no-op
            },
            onRemove = {
                // no-op
            },
            modifier = Modifier.padding(16.dp)
        )
    }
}
