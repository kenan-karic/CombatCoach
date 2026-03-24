package io.aethibo.combatcoach.features.progress

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import io.aethibo.combatcoach.features.progress.model.LogEntryUi
import io.aethibo.combatcoach.features.progress.model.WeekDay
import io.aethibo.combatcoach.shared.combo.domain.model.Combo
import io.aethibo.combatcoach.shared.combo.domain.usecase.ObserveCombosUseCase
import io.aethibo.combatcoach.shared.log.domain.model.DashboardStats
import io.aethibo.combatcoach.shared.log.domain.model.WorkoutLog
import io.aethibo.combatcoach.shared.log.domain.usecase.DeleteWorkoutLogUseCase
import io.aethibo.combatcoach.shared.log.domain.usecase.ObserveDashboardStatsUseCase
import io.aethibo.combatcoach.shared.log.domain.usecase.ObserveWorkoutLogsUseCase
import io.aethibo.combatcoach.shared.utils.Discipline
import io.aethibo.combatcoach.shared.utils.startOfMonthEpoch
import io.aethibo.combatcoach.shared.utils.startOfWeekEpoch
import io.aethibo.combatcoach.shared.utils.toDisplayDate
import io.aethibo.combatcoach.shared.workout.domain.model.Workout
import io.aethibo.combatcoach.shared.workout.domain.model.WorkoutType
import io.aethibo.combatcoach.shared.workout.domain.usecase.ObserveWorkoutsUseCase
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.Calendar

private data class ProgressData(
    val logs: List<WorkoutLog> = emptyList(),
    val workouts: List<Workout> = emptyList(),
    val combos: List<Combo> = emptyList(),
    val stats: DashboardStats = DashboardStats(),
)

@Composable
fun progressPresenter(
    observeLogs: ObserveWorkoutLogsUseCase,
    observeWorkouts: ObserveWorkoutsUseCase,
    observeCombos: ObserveCombosUseCase,
    observeDashboard: ObserveDashboardStatsUseCase,
    deleteLog: DeleteWorkoutLogUseCase,
    onNavigateToWorkout: (Int) -> Unit,
): ProgressState {
    val scope = rememberCoroutineScope()

    var activeFilter by remember { mutableStateOf(ProgressFilter.ALL) }
    var selectedEntry by remember { mutableStateOf<LogEntryUi?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    val data by remember {
        combine(
            observeLogs(),
            observeWorkouts(),
            observeCombos(),
            observeDashboard(),
        ) { logs, workouts, combos, stats ->
            isLoading = false
            ProgressData(logs, workouts, combos, stats)
        }
    }.collectAsState(initial = ProgressData())

    val allEntries = remember(data.logs, data.workouts, data.combos) {
        val workoutMap = data.workouts.associateBy { it.id }
        val comboMap = data.combos.associateBy { it.id }

        data.logs.mapNotNull { log ->
            val workout = log.workoutId?.let { workoutMap[it] }
            val combo = log.comboId?.let { comboMap[it] }
            val name = workout?.name ?: combo?.name ?: return@mapNotNull null
            val disc = workout?.workoutDiscipline ?: combo?.discipline ?: Discipline.GENERAL
            val type = if (log.workoutId != null) WorkoutType.STRENGTH else WorkoutType.CIRCUIT

            val previousLog = data.logs
                .filter { other ->
                    other.id != log.id &&
                            other.completedAt < log.completedAt &&
                            (other.workoutId == log.workoutId || other.comboId == log.comboId)
                }
                .maxByOrNull { it.completedAt }

            LogEntryUi(
                log = log,
                workoutName = name,
                discipline = disc,
                type = type,
                previousLog = previousLog,
            )
        }
    }

    val filteredEntries = remember(allEntries, activeFilter) {
        val weekStart = startOfWeekEpoch()
        val monthStart = startOfMonthEpoch()
        when (activeFilter) {
            ProgressFilter.ALL -> allEntries
            ProgressFilter.WORKOUTS -> allEntries.filter { it.type == WorkoutType.STRENGTH }
            ProgressFilter.COMBOS -> allEntries.filter { it.type == WorkoutType.CIRCUIT }
            ProgressFilter.THIS_WEEK -> allEntries.filter { it.log.completedAt >= weekStart }
            ProgressFilter.THIS_MONTH -> allEntries.filter { it.log.completedAt >= monthStart }
        }
    }

    val groupedEntries = remember(filteredEntries) {
        filteredEntries.groupBy { it.log.completedAt.toDisplayDate() }
    }

    val weekDays = remember(allEntries) { buildWeekDays(allEntries) }

    val eventSink: (ProgressEvent) -> Unit = remember {
        { event ->
            when (event) {
                is ProgressEvent.FilterSelected -> activeFilter = event.filter
                is ProgressEvent.EntryTapped -> selectedEntry = event.entry
                ProgressEvent.DismissDetail -> selectedEntry = null

                is ProgressEvent.DeleteLog -> {
                    selectedEntry = null
                    scope.launch { deleteLog(event.logId) }
                }
            }
        }
    }

    return ProgressState(
        stats = data.stats,
        allEntries = allEntries,
        filteredEntries = filteredEntries,
        groupedEntries = groupedEntries,
        weekDays = weekDays,
        activeFilter = activeFilter,
        selectedEntry = selectedEntry,
        isLoading = isLoading,
        eventSink = eventSink,
    )
}

// ── Pure helpers ───────────────────────────────────────────────────────────

private fun buildWeekDays(entries: List<LogEntryUi>): List<WeekDay> {
    val cal = Calendar.getInstance()
    val today = cal.get(Calendar.DAY_OF_WEEK)
    val dayNames = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    val weekStart = startOfWeekEpoch()

    val countMap = entries
        .filter { it.log.completedAt >= weekStart }
        .groupBy { entry ->
            Calendar.getInstance()
                .also { it.timeInMillis = entry.log.completedAt }
                .get(Calendar.DAY_OF_WEEK)
        }
        .mapValues { it.value.size }

    return (Calendar.SUNDAY..Calendar.SATURDAY).map { dow ->
        WeekDay(
            label = dayNames[dow - 1],
            sessionCount = countMap[dow] ?: 0,
            isToday = dow == today,
        )
    }
}
