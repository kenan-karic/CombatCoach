package io.aethibo.combatcoach.features.progress

import io.aethibo.combatcoach.features.progress.model.WeekDay
import io.aethibo.combatcoach.shared.log.domain.model.DashboardStats
import io.aethibo.combatcoachex.features.progress.presentation.model.LogEntryUi
import io.aethibo.combatcoachex.features.progress.presentation.model.WeekDay
import io.aethibo.combatcoachex.features.shared.log.domain.model.DashboardStats

enum class ProgressFilter { ALL, WORKOUTS, COMBOS, THIS_WEEK, THIS_MONTH }

fun ProgressFilter.label(): String = when (this) {
    ProgressFilter.ALL -> "All"
    ProgressFilter.WORKOUTS -> "Workouts"
    ProgressFilter.COMBOS -> "Combos"
    ProgressFilter.THIS_WEEK -> "This week"
    ProgressFilter.THIS_MONTH -> "This month"
}

data class ProgressState(
    val stats: DashboardStats = DashboardStats(),
    val allEntries: List<LogEntryUi> = emptyList(),
    val filteredEntries: List<LogEntryUi> = emptyList(),
    val groupedEntries: Map<String, List<LogEntryUi>> = emptyMap(),
    val weekDays: List<WeekDay> = emptyList(),
    val activeFilter: ProgressFilter = ProgressFilter.ALL,
    val selectedEntry: LogEntryUi? = null,
    val isLoading: Boolean = true,
    val eventSink: (ProgressEvent) -> Unit = {},
)
