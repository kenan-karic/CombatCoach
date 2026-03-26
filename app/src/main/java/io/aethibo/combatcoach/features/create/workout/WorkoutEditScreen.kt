package io.aethibo.combatcoach.features.create.workout

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.R
import io.aethibo.combatcoach.core.ui.components.NumberField
import io.aethibo.combatcoach.core.ui.components.WorkoutDisciplineSelector
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing
import io.aethibo.combatcoach.features.create.components.AddExerciseSheet
import io.aethibo.combatcoach.features.create.components.DeleteConfirmPopup
import io.aethibo.combatcoach.features.create.components.DiscardChangesPopup
import io.aethibo.combatcoach.features.create.components.EditTopBar
import io.aethibo.combatcoach.features.create.components.ExerciseRow
import io.aethibo.combatcoach.features.create.components.NameDescriptionSection
import io.aethibo.combatcoach.features.create.components.SaveBottomBar
import io.aethibo.combatcoach.features.create.components.SectionDivider
import io.aethibo.combatcoach.features.create.components.TypeToggle
import io.aethibo.combatcoach.features.create.utils.CreateEditMode
import io.aethibo.combatcoach.shared.workout.domain.model.WorkoutType

@Composable
fun WorkoutEditScreen(
    state: WorkoutEditState,
    onBack: () -> Unit,
) {
    val sp = LocalSpacing.current
    var showAddExerciseSheet by remember { mutableStateOf(false) }

    BackHandler { state.eventSink(WorkoutEditEvent.RequestBack) }

    if (state.isLoading) {
        Box(Modifier.fillMaxSize(), Alignment.Center) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        return
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            EditTopBar(
                title = if (state.mode is CreateEditMode.Create)
                    stringResource(R.string.workout_edit_title_create)
                else
                    stringResource(R.string.workout_edit_title_edit),
                onBack = { state.eventSink(WorkoutEditEvent.RequestBack) },
                onDelete = if (state.mode is CreateEditMode.Edit) {
                    { state.eventSink(WorkoutEditEvent.Delete) }
                } else null,
                onStart = if (state.mode is CreateEditMode.Edit) {
                    { state.eventSink(WorkoutEditEvent.StartTimer) }
                } else null,
                startLabel = stringResource(R.string.workout_edit_action_start),
            )
        },
        bottomBar = {
            SaveBottomBar(
                isSaving = state.isSaving,
                onSave = { state.eventSink(WorkoutEditEvent.Save) },
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(vertical = sp.medium),
        ) {

            // ── Name & description ────────────────────────────────────────
            item {
                NameDescriptionSection(
                    name = state.name,
                    description = state.description,
                    nameError = state.nameError,
                    onNameChange = { state.eventSink(WorkoutEditEvent.NameChanged(it)) },
                    onDescChange = { state.eventSink(WorkoutEditEvent.DescriptionChanged(it)) },
                )
            }

            // ── Discipline (workout-specific options) ─────────────────────
            item {
                SectionDivider(stringResource(R.string.workout_edit_section_discipline))
                WorkoutDisciplineSelector(
                    selected = state.workoutDiscipline,
                    onSelect = { state.eventSink(WorkoutEditEvent.WorkoutDisciplineChanged(it)) },
                    modifier = Modifier.padding(horizontal = sp.screenPadding),
                )
            }

            // ── Type toggle ───────────────────────────────────────────────
            item {
                SectionDivider(stringResource(R.string.workout_edit_section_type))
                TypeToggle(
                    selected = state.workoutType,
                    onSelect = { state.eventSink(WorkoutEditEvent.TypeChanged(it)) },
                    modifier = Modifier.padding(horizontal = sp.screenPadding),
                )
            }

            // ── Circuit settings ──────────────────────────────────────────
            if (state.workoutType == WorkoutType.CIRCUIT) {
                item {
                    SectionDivider(stringResource(R.string.workout_edit_section_circuit_settings))
                    Row(
                        modifier = Modifier.padding(horizontal = sp.screenPadding),
                        horizontalArrangement = Arrangement.spacedBy(sp.small),
                    ) {
                        NumberField(
                            label = stringResource(R.string.workout_edit_label_rounds),
                            value = state.circuitRounds,
                            onValue = { state.eventSink(WorkoutEditEvent.CircuitRoundsChanged(it)) },
                            modifier = Modifier.weight(1f),
                        )
                        NumberField(
                            label = stringResource(R.string.workout_edit_label_rest_rounds),
                            value = state.circuitRestBetweenRoundsSeconds,
                            onValue = { state.eventSink(WorkoutEditEvent.CircuitRestChanged(it)) },
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }

            // ── Exercises ─────────────────────────────────────────────────
            item {
                SectionDivider(
                    label = when (state.workoutType) {
                        WorkoutType.CIRCUIT -> stringResource(
                            R.string.workout_edit_section_stations,
                            state.exercises.size
                        )

                        WorkoutType.STRENGTH -> stringResource(
                            R.string.workout_edit_section_exercises,
                            state.exercises.size
                        )
                    }
                )
            }

            itemsIndexed(
                state.exercises,
                key = { index, ex -> "${ex.id}_${index}" }) { index, exercise ->
                ExerciseRow(
                    exercise = exercise,
                    index = index,
                    total = state.exercises.size,
                    workoutType = state.workoutType,
                    onUpdate = { state.eventSink(WorkoutEditEvent.UpdateExercise(it)) },
                    onRemove = { state.eventSink(WorkoutEditEvent.RemoveExercise(exercise.id)) },
                    onMoveUp = {
                        state.eventSink(
                            WorkoutEditEvent.ReorderExercises(
                                index,
                                index - 1
                            )
                        )
                    },
                    onMoveDown = {
                        state.eventSink(
                            WorkoutEditEvent.ReorderExercises(
                                index,
                                index + 1
                            )
                        )
                    },
                    modifier = Modifier.padding(horizontal = sp.screenPadding),
                )
                Spacer(Modifier.height(sp.cardGap))
            }

            item {
                TextButton(
                    onClick = { showAddExerciseSheet = true },
                    modifier = Modifier.padding(horizontal = sp.screenPadding),
                ) {
                    Icon(Icons.Filled.Add, null, Modifier.size(18.dp))
                    Spacer(Modifier.width(sp.xxs))
                    Text(
                        when (state.workoutType) {
                            WorkoutType.CIRCUIT -> stringResource(R.string.workout_edit_action_add_station)
                            WorkoutType.STRENGTH -> stringResource(R.string.workout_edit_action_add_exercise)
                        }
                    )
                }
                Spacer(Modifier.height(sp.xl))
            }
        }
    }

    if (showAddExerciseSheet) {
        AddExerciseSheet(
            workoutType = state.workoutType,
            onAdd = {
                state.eventSink(WorkoutEditEvent.AddExercise(it))
                showAddExerciseSheet = false
            },
            onDismiss = { showAddExerciseSheet = false },
        )
    }

    if (state.showDeleteDialog) {
        DeleteConfirmPopup(
            itemName = state.name,
            onConfirm = { state.eventSink(WorkoutEditEvent.ConfirmDelete) },
            onDismissRequest = { state.eventSink(WorkoutEditEvent.DismissDelete) },
        )
    }

    if (state.showDiscardDialog) {
        DiscardChangesPopup(
            onDiscard = { state.eventSink(WorkoutEditEvent.ConfirmDiscard) },
            onDismissRequest = { state.eventSink(WorkoutEditEvent.DismissDiscard) },
        )
    }
}
