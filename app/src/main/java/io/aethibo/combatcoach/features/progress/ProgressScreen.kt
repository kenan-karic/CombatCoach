package io.aethibo.combatcoach.features.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme
import io.aethibo.combatcoach.core.ui.theme.DevicesPreview
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing
import io.aethibo.combatcoach.features.progress.components.ProgressHeader
import io.aethibo.combatcoach.shared.log.domain.model.DashboardStats
import io.aethibo.combatcoach.shared.log.domain.model.WorkoutLog
import io.aethibo.combatcoach.shared.utils.Discipline
import io.aethibo.combatcoach.shared.workout.domain.model.WorkoutType
import io.aethibo.combatcoachex.features.progress.presentation.components.FilterBar
import io.aethibo.combatcoachex.features.progress.presentation.components.LogEntryCard
import io.aethibo.combatcoachex.features.progress.presentation.components.ProgressEmptyState
import io.aethibo.combatcoachex.features.progress.presentation.components.ProgressHeader
import io.aethibo.combatcoachex.features.progress.presentation.components.WorkoutHistorySheet
import io.aethibo.combatcoachex.features.progress.presentation.model.LogEntryUi
import io.aethibo.combatcoachex.features.progress.presentation.model.WeekDay
import kotlin.collections.emptyList
import kotlin.collections.emptyMap
import kotlin.collections.mapOf
import kotlin.to

@Composable
fun ProgressScreen(state: ProgressState) {
    val sp = LocalSpacing.current

    if (state.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = sp.xxl),
    ) {
        item { ProgressHeader(stats = state.stats, weekDays = state.weekDays) }

        item {
            FilterBar(
                activeFilter = state.activeFilter,
                onSelect = { state.eventSink(ProgressEvent.FilterSelected(it)) },
                modifier = Modifier.padding(horizontal = sp.screenPadding),
            )
            Spacer(Modifier.height(sp.medium))
        }

        item {
            Text(
                text = "${state.filteredEntries.size} sessions",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = sp.screenPadding),
            )
            Spacer(Modifier.height(sp.small))
        }

        if (state.filteredEntries.isEmpty()) {
            item {
                ProgressEmptyState(
                    filter = state.activeFilter,
                    modifier = Modifier.padding(sp.screenPadding),
                )
            }
        } else {
            // FIX: groupedEntries is pre-computed in the Presenter.
            // The Screen just iterates — no groupBy() on every scroll frame.
            state.groupedEntries.forEach { (dateLabel, entries) ->
                item(key = "header-$dateLabel") {
                    Text(
                        text = dateLabel,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(
                            horizontal = sp.screenPadding,
                            vertical = sp.xs,
                        ),
                    )
                }
                items(entries, key = { it.log.id }) { entry ->
                    LogEntryCard(
                        entry = entry,
                        onTap = { state.eventSink(ProgressEvent.EntryTapped(entry)) },
                        modifier = Modifier.padding(horizontal = sp.screenPadding),
                    )
                    Spacer(Modifier.height(sp.cardGap))
                }
            }
        }
    }

    state.selectedEntry?.let { entry ->
        WorkoutHistorySheet(
            entry = entry,
            onDelete = { state.eventSink(ProgressEvent.DeleteLog(entry.log.id)) },
            onDismiss = { state.eventSink(ProgressEvent.DismissDetail) },
        )
    }
}

@Preview(showBackground = true, name = "Progress screen loading")
@Composable
private fun ProgressScreenLoadingPreview() {
    CombatCoachTheme {
        ProgressScreen(
            state = ProgressState(isLoading = true)
        )
    }
}

@Preview(showBackground = true, name = "Progress screen empty")
@Composable
private fun ProgressScreenEmptyPreview() {
    CombatCoachTheme {
        ProgressScreen(
            state = ProgressState(
                isLoading = false,
                stats = previewStats(),
                weekDays = previewWeekDays(),
                activeFilter = ProgressFilter.ALL,
                filteredEntries = emptyList(),
                groupedEntries = emptyMap(),
            )
        )
    }
}

@DevicesPreview
@Composable
private fun ProgressScreenContentPreview() {
    CombatCoachTheme {
        val entry1 = previewLogEntry(1, "Boxing Fundamentals")
        val entry2 = previewLogEntry(2, "Heavy Bag Session")

        ProgressScreen(
            state = ProgressState(
                isLoading = false,
                stats = previewStats(),
                weekDays = previewWeekDays(),
                filteredEntries = listOf(entry1, entry2),
                groupedEntries = mapOf(
                    "Today" to listOf(entry1),
                    "Yesterday" to listOf(entry2),
                ),
                activeFilter = ProgressFilter.ALL,
            )
        )
    }
}

@Preview(showBackground = true, name = "Progress screen detail open")
@Composable
private fun ProgressScreenDetailOpenPreview() {
    CombatCoachTheme {
        val entry = previewLogEntry(1, "Boxing Fundamentals")

        ProgressScreen(
            state = ProgressState(
                isLoading = false,
                stats = previewStats(),
                weekDays = previewWeekDays(),
                filteredEntries = listOf(entry),
                groupedEntries = mapOf("Today" to listOf(entry)),
                selectedEntry = entry,
            )
        )
    }
}

private fun previewWeekDays(): List<WeekDay> =
    listOf(
        WeekDay("Sun", 1, false),
        WeekDay("Mon", 0, false),
        WeekDay("Tue", 2, false),
        WeekDay("Wed", 1, true),
        WeekDay("Thu", 0, false),
        WeekDay("Fri", 1, false),
        WeekDay("Sat", 0, false),
    )

private fun previewStats() = DashboardStats(
    workoutsThisWeek = 24,
    totalMinutes = 980,
    currentStreak = 5,
)

private fun previewLogEntry(id: Int, name: String): LogEntryUi {
    val log = WorkoutLog(
        id = id,
        workoutId = 1,
        comboId = null,
        completedAt = System.currentTimeMillis(),
        durationSeconds = 1800,
        exerciseLogs = emptyList(),
        notes = "",
    )

    return LogEntryUi(
        log = log,
        workoutName = name,
        discipline = Discipline.STRIKING,
        type = WorkoutType.STRENGTH,
        previousLog = null,
    )
}
