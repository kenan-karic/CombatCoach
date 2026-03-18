package io.aethibo.combatcoach.features.create.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.R
import io.aethibo.combatcoach.core.ui.components.ccTextFieldColors
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing
import io.aethibo.combatcoach.shared.combo.domain.model.Combo
import io.aethibo.combatcoach.shared.plan.domain.model.PlanDay
import io.aethibo.combatcoach.shared.workout.domain.model.Workout

@Composable
fun PlanDayRow(
    day: PlanDay,
    index: Int,
    isExpanded: Boolean,
    availableWorkouts: List<Workout>,
    availableCombos: List<Combo>,
    onToggleExpand: () -> Unit,
    onLabelChange: (String) -> Unit,
    onToggleRest: () -> Unit,
    onAddWorkout: (Int) -> Unit,
    onRemoveWorkout: (Int) -> Unit,
    onAddCombo: (Int) -> Unit,
    onRemoveCombo: (Int) -> Unit,
    onRemoveDay: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sp = LocalSpacing.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(12.dp),
                spotColor = Color.Black.copy(alpha = 0.05f),
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleExpand() }
                    .padding(sp.medium),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(
                            if (day.isRestDay) MaterialTheme.colorScheme.tertiaryContainer
                            else MaterialTheme.colorScheme.primaryContainer
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        "${index + 1}",
                        style = MaterialTheme.typography.labelMedium,
                        color = if (day.isRestDay) MaterialTheme.colorScheme.tertiary
                        else MaterialTheme.colorScheme.primary,
                    )
                }
                Spacer(Modifier.width(sp.small))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        day.label,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    if (day.isRestDay) {
                        Text(
                            text = stringResource(R.string.plan_day_rest_label),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    } else {
                        Text(
                            text = stringResource(
                                R.string.plan_day_summary_format,
                                day.workoutIds.size,
                                day.comboIds.size
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                IconButton(onClick = onRemoveDay) {
                    Icon(
                        Icons.Filled.Close, "Remove day", Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Icon(
                    if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp),
                )
            }

            // Expanded content
            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier.padding(
                        start = sp.medium, end = sp.medium, bottom = sp.medium
                    ),
                    verticalArrangement = Arrangement.spacedBy(sp.small),
                ) {
                    OutlinedTextField(
                        value = day.label,
                        onValueChange = onLabelChange,
                        label = { Text(stringResource(R.string.plan_day_field_label)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true,
                        colors = ccTextFieldColors(),
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "Rest day", style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                        Switch(
                            checked = day.isRestDay,
                            onCheckedChange = { onToggleRest() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.tertiary,
                                checkedTrackColor = MaterialTheme.colorScheme.tertiaryContainer,
                            ),
                        )
                    }

                    if (!day.isRestDay) {
                        // Workouts picker
                        Text(
                            text = stringResource(R.string.plan_day_header_workouts),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        availableWorkouts.forEach { workout ->
                            val added = workout.id in day.workoutIds
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    workout.name,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.weight(1f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                                Checkbox(
                                    checked = added,
                                    onCheckedChange = { checked ->
                                        workout.id?.let {
                                            if (checked) onAddWorkout(workout.id) else onRemoveWorkout(
                                                workout.id
                                            )
                                        }
                                    },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = MaterialTheme.colorScheme.primary,
                                    ),
                                )
                            }
                        }

                        // Combos picker
                        if (availableCombos.isNotEmpty()) {
                            Text(
                                text = stringResource(R.string.plan_day_header_workouts),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            availableCombos.forEach { combo ->
                                val added = combo.id in day.comboIds
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(
                                        combo.name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.weight(1f),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                    Checkbox(
                                        checked = added,
                                        onCheckedChange = { checked ->
                                            if (checked) {
                                                onAddCombo(combo.id)
                                            } else {
                                                onRemoveCombo(combo.id)
                                            }
                                        },
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = MaterialTheme.colorScheme.secondary,
                                        ),
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

@Preview(name = "Plan Day States - Light", showBackground = true)
@Composable
private fun PlanDayRowPreview() {
    CombatCoachTheme {
        // Mock Data
        val mockWorkouts = listOf(
            Workout(id = 1, name = "Heavy Bag Power"),
            Workout(id = 2, name = "Speed Footwork")
        )
        val mockCombos = listOf(
            Combo(id = 101, name = "Thai Clinch Knee Drill")
        )

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // State 1: Training Day (Expanded)
            PlanDayRow(
                day = PlanDay(
                    label = "Explosive Monday",
                    isRestDay = false,
                    workoutIds = listOf(1),
                    comboIds = listOf(101),
                    dayNumber = 3,
                ),
                index = 0,
                isExpanded = true,
                availableWorkouts = mockWorkouts,
                availableCombos = mockCombos,
                onToggleExpand = {}, onLabelChange = {}, onToggleRest = {},
                onAddWorkout = {}, onRemoveWorkout = {},
                onAddCombo = {}, onRemoveCombo = {}, onRemoveDay = {}
            )

            // State 2: Rest Day (Collapsed)
            PlanDayRow(
                day = PlanDay(
                    label = "Recovery Day",
                    isRestDay = true,
                    dayNumber = 2
                ),
                index = 1,
                isExpanded = false,
                availableWorkouts = emptyList(),
                availableCombos = emptyList(),
                onToggleExpand = {}, onLabelChange = {}, onToggleRest = {},
                onAddWorkout = {}, onRemoveWorkout = {},
                onAddCombo = {}, onRemoveCombo = {}, onRemoveDay = {}
            )
        }
    }
}
