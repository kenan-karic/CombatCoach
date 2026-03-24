package io.aethibo.combatcoach.features.progress.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import io.aethibo.combatcoach.core.ui.popup.CoachPopup
import io.aethibo.combatcoach.core.ui.popup.PopupTypes
import io.aethibo.combatcoach.core.ui.popup.defaultPopupButtons
import io.aethibo.combatcoach.core.ui.popup.defaultPopupContent
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing
import io.aethibo.combatcoach.features.progress.model.LogEntryUi
import io.aethibo.combatcoach.shared.utils.toDisplayDateTime
import io.aethibo.combatcoach.shared.utils.toMinutesSeconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WorkoutHistorySheet(
    entry: LogEntryUi,
    onDelete: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sp = LocalSpacing.current
    var showDeleteConfirm by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = sp.screenPadding)
                .padding(bottom = sp.xxxl)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(sp.medium),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = entry.workoutName,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        text = entry.log.completedAt.toDisplayDateTime(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                IconButton(onClick = { showDeleteConfirm = true }) {
                    Icon(
                        Icons.Filled.DeleteOutline,
                        contentDescription = stringResource(R.string.cd_delete_log),
                        tint = MaterialTheme.colorScheme.error,
                    )
                }
            }

            // Summary stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(sp.small),
            ) {
                DetailStatChip(
                    label = stringResource(R.string.history_label_duration),
                    value = entry.log.durationSeconds.toMinutesSeconds(),
                    modifier = Modifier.weight(1f),
                )
                if (entry.log.exerciseLogs.isNotEmpty()) {
                    DetailStatChip(
                        label = stringResource(R.string.history_label_sets),
                        value = entry.log.exerciseLogs.sumOf { it.setsCompleted }.toString(),
                        modifier = Modifier.weight(1f),
                    )
                    DetailStatChip(
                        label = stringResource(R.string.history_label_reps),
                        value = entry.log.exerciseLogs.sumOf { it.totalReps }.toString(),
                        modifier = Modifier.weight(1f),
                    )
                }
            }

            // Previous session comparison
            entry.previousLog?.let { prev ->
                PreviousComparisonRow(
                    current = entry.log,
                    previous = prev,
                )
            }

            // Exercise breakdown
            if (entry.log.exerciseLogs.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.history_header_breakdown),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                entry.log.exerciseLogs.forEach { exLog ->
                    ExerciseLogRow(exLog = exLog)
                }
            }

            // Notes
            if (entry.log.notes.isNotBlank()) {
                Text(
                    text = stringResource(R.string.history_header_notes),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                ) {
                    Text(
                        text = entry.log.notes,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(sp.medium),
                    )
                }
            }
        }
    }

    if (showDeleteConfirm) {
        CoachPopup(
            content = defaultPopupContent(
                titleResId = R.string.history_delete_title,
                descriptionResId = R.string.history_delete_message
            ),
            buttons = defaultPopupButtons(
                confirmResId = R.string.action_delete,
                confirmButtonAction = {
                    showDeleteConfirm = false
                    onDelete()
                }
                // No cancel button - will use full width
            ),
            popupType = PopupTypes.Error,
            onDismissRequest = {
                showDeleteConfirm = false
            }
        )
    }
}
