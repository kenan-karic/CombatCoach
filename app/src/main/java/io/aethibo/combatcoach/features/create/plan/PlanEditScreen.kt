package io.aethibo.combatcoach.features.create.plan

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.R
import io.aethibo.combatcoach.core.ui.components.DisciplineSelector
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing
import io.aethibo.combatcoach.features.create.components.CollectionWorkoutPicker
import io.aethibo.combatcoach.features.create.components.DeleteConfirmPopup
import io.aethibo.combatcoach.features.create.components.EditTopBar
import io.aethibo.combatcoach.features.create.components.NameDescriptionSection
import io.aethibo.combatcoach.features.create.components.PlanDayRow
import io.aethibo.combatcoach.features.create.components.PlanTypeToggle
import io.aethibo.combatcoach.features.create.components.SaveBottomBar
import io.aethibo.combatcoach.features.create.components.SectionDivider
import io.aethibo.combatcoach.features.create.utils.CreateEditMode
import io.aethibo.combatcoach.shared.plan.domain.model.PlanType

@Composable
fun PlanEditScreen(
    state: PlanEditState,
    onBack: () -> Unit,
) {
    val sp = LocalSpacing.current

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
                    stringResource(R.string.plan_edit_title_create)
                else
                    stringResource(R.string.plan_edit_title_edit),
                onBack = onBack,
                onDelete = if (state.mode is CreateEditMode.Edit) {
                    { state.eventSink(PlanEditEvent.Delete) }
                } else null,
            )
        },
        bottomBar = {
            SaveBottomBar(
                isSaving = state.isSaving,
                onSave = { state.eventSink(PlanEditEvent.Save) },
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(vertical = sp.medium),
        ) {
            item {
                NameDescriptionSection(
                    name = state.name,
                    description = state.description,
                    nameError = state.nameError,
                    onNameChange = { state.eventSink(PlanEditEvent.NameChanged(it)) },
                    onDescChange = { state.eventSink(PlanEditEvent.DescriptionChanged(it)) },
                )
            }

            item {
                SectionDivider(stringResource(R.string.plan_edit_section_discipline))
                DisciplineSelector(
                    selected = state.discipline,
                    onSelect = { state.eventSink(PlanEditEvent.DisciplineChanged(it)) },
                    modifier = Modifier.padding(horizontal = sp.screenPadding),
                )
            }

            item {
                SectionDivider(stringResource(R.string.plan_edit_section_type))
                PlanTypeToggle(
                    selected = state.planType,
                    onSelect = { state.eventSink(PlanEditEvent.TypeChanged(it)) },
                    modifier = Modifier.padding(horizontal = sp.screenPadding),
                )
            }

            if (state.planType == PlanType.PROGRAM) {
                item {
                    SectionDivider(stringResource(R.string.plan_edit_section_days, state.days.size))
                }

                itemsIndexed(
                    state.days,
                    key = { index, day -> "${day.dayNumber}_${index}" }) { index, day ->
                    PlanDayRow(
                        day = day,
                        index = index,
                        isExpanded = state.expandedDayIndex == index,
                        availableWorkouts = state.availableWorkouts,
                        availableCombos = state.availableCombos,
                        onToggleExpand = { state.eventSink(PlanEditEvent.ToggleExpandDay(index)) },
                        onLabelChange = {
                            state.eventSink(
                                PlanEditEvent.UpdateDayLabel(
                                    index,
                                    it
                                )
                            )
                        },
                        onToggleRest = { state.eventSink(PlanEditEvent.ToggleDayRest(index)) },
                        onAddWorkout = {
                            state.eventSink(
                                PlanEditEvent.AddWorkoutToDay(
                                    index,
                                    it
                                )
                            )
                        },
                        onRemoveWorkout = {
                            state.eventSink(
                                PlanEditEvent.RemoveWorkoutFromDay(
                                    index,
                                    it
                                )
                            )
                        },
                        onAddCombo = { state.eventSink(PlanEditEvent.AddComboToDay(index, it)) },
                        onRemoveCombo = {
                            state.eventSink(
                                PlanEditEvent.RemoveComboFromDay(
                                    index,
                                    it
                                )
                            )
                        },
                        onRemoveDay = { state.eventSink(PlanEditEvent.RemoveDay(index)) },
                        modifier = Modifier.padding(horizontal = sp.screenPadding),
                    )
                    Spacer(Modifier.height(sp.cardGap))
                }

                item {
                    TextButton(
                        onClick = { state.eventSink(PlanEditEvent.AddDay) },
                        modifier = Modifier.padding(horizontal = sp.screenPadding),
                    ) {
                        Icon(Icons.Filled.Add, null, Modifier.size(18.dp))
                        Spacer(Modifier.width(sp.xxs))
                        Text(stringResource(R.string.plan_edit_action_add_day))
                    }
                }
            } else {
                // Collection type — pick workouts
                item {
                    SectionDivider(stringResource(R.string.plan_edit_section_collection))
                    CollectionWorkoutPicker(
                        selectedIds = state.collectionWorkoutIds,
                        availableWorkouts = state.availableWorkouts,
                        onAdd = { state.eventSink(PlanEditEvent.AddToCollection(it)) },
                        onRemove = { state.eventSink(PlanEditEvent.RemoveFromCollection(it)) },
                        modifier = Modifier.padding(horizontal = sp.screenPadding),
                    )
                }
            }

            item { Spacer(Modifier.height(sp.xl)) }
        }
    }

    if (state.showDeleteDialog) {
        DeleteConfirmPopup(
            itemName = state.name,
            onConfirm = { state.eventSink(PlanEditEvent.ConfirmDelete) },
            onDismissRequest = { state.eventSink(PlanEditEvent.DismissDelete) },
        )
    }
}
