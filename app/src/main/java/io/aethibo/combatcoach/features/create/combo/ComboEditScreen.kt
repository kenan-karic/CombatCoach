package io.aethibo.combatcoach.features.create.combo

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.R
import io.aethibo.combatcoach.core.ui.components.DisciplineSelector
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing
import io.aethibo.combatcoach.features.create.components.AddStrikePopup
import io.aethibo.combatcoach.features.create.components.ComboTimerSettings
import io.aethibo.combatcoach.features.create.components.DeleteConfirmPopup
import io.aethibo.combatcoach.features.create.components.EditTopBar
import io.aethibo.combatcoach.features.create.components.NameDescriptionSection
import io.aethibo.combatcoach.features.create.components.SaveBottomBar
import io.aethibo.combatcoach.features.create.components.SectionDivider
import io.aethibo.combatcoach.features.create.components.StrikeRow
import io.aethibo.combatcoach.features.create.utils.CreateEditMode

@Composable
fun ComboEditScreen(
    state: ComboEditState,
    onBack: () -> Unit,
) {
    val sp = LocalSpacing.current
    var showAddStrikeDialog by remember { mutableStateOf(false) }

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
                    stringResource(R.string.combo_edit_title_create)
                else
                    stringResource(R.string.combo_edit_title_edit),
                onBack = onBack,
                onDelete = if (state.mode is CreateEditMode.Edit) {
                    { state.eventSink(ComboEditEvent.Delete) }
                } else null,
                onStart = if (state.mode is CreateEditMode.Edit) {
                    { state.eventSink(ComboEditEvent.StartTimer) }
                } else null,
                startLabel = stringResource(R.string.combo_edit_action_drill),
            )
        },
        bottomBar = {
            SaveBottomBar(
                isSaving = state.isSaving,
                onSave = { state.eventSink(ComboEditEvent.Save) },
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
                    onNameChange = { state.eventSink(ComboEditEvent.NameChanged(it)) },
                    onDescChange = { state.eventSink(ComboEditEvent.DescriptionChanged(it)) },
                )
            }

            item {
                SectionDivider(stringResource(R.string.combo_edit_section_discipline))
                DisciplineSelector(
                    selected = state.discipline,
                    onSelect = { state.eventSink(ComboEditEvent.DisciplineChanged(it)) },
                    modifier = Modifier.padding(horizontal = sp.screenPadding),
                )
            }

            // Timer settings
            item {
                SectionDivider(stringResource(R.string.combo_edit_section_timer_settings))
                ComboTimerSettings(
                    durationSeconds = state.durationSeconds,
                    rounds = state.rounds,
                    restSeconds = state.restBetweenRoundsSeconds,
                    advanceMode = state.advanceMode,
                    onDuration = { state.eventSink(ComboEditEvent.DurationChanged(it)) },
                    onRounds = { state.eventSink(ComboEditEvent.RoundsChanged(it)) },
                    onRest = { state.eventSink(ComboEditEvent.RestChanged(it)) },
                    onAdvanceMode = { state.eventSink(ComboEditEvent.AdvanceModeChanged(it)) },
                    modifier = Modifier.padding(horizontal = sp.screenPadding),
                )
            }

            // Strikes
            item {
                stringResource(R.string.combo_edit_section_sequence, state.strikes.size)
            }

            itemsIndexed(state.strikes, key = { i, s -> "$i-$s" }) { index, strike ->
                StrikeRow(
                    strike = strike,
                    index = index,
                    total = state.strikes.size,
                    onRemove = { state.eventSink(ComboEditEvent.RemoveStrike(index)) },
                    modifier = Modifier.padding(horizontal = sp.screenPadding),
                )
                Spacer(Modifier.height(sp.cardGap))
            }

            item {
                TextButton(
                    onClick = { showAddStrikeDialog = true },
                    modifier = Modifier.padding(horizontal = sp.screenPadding),
                ) {
                    Icon(Icons.Filled.Add, null, Modifier.size(18.dp))
                    Spacer(Modifier.width(sp.xxs))
                    Text(stringResource(R.string.combo_edit_action_add_strike))
                }
                Spacer(Modifier.height(sp.xl))
            }
        }
    }

    if (showAddStrikeDialog) {
        AddStrikePopup(
            onAdd = { state.eventSink(ComboEditEvent.AddStrike(it)); showAddStrikeDialog = false },
            onDismissRequest = { showAddStrikeDialog = false },
        )
    }

    if (state.showDeleteDialog) {
        DeleteConfirmPopup(
            itemName = state.name,
            onConfirm = { state.eventSink(ComboEditEvent.ConfirmDelete) },
            onDismissRequest = { state.eventSink(ComboEditEvent.DismissDelete) },
        )
    }
}
