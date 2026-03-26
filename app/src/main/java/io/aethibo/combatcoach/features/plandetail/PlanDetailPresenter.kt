package io.aethibo.combatcoach.features.plandetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import io.aethibo.combatcoach.features.plandetail.model.PlanDetailData
import io.aethibo.combatcoach.features.plandetail.model.SessionType
import io.aethibo.combatcoach.features.plandetail.model.WeekGroup
import io.aethibo.combatcoach.shared.combo.domain.usecase.ObserveCombosUseCase
import io.aethibo.combatcoach.shared.plan.domain.model.ActivePlan
import io.aethibo.combatcoach.shared.plan.domain.model.Plan
import io.aethibo.combatcoach.shared.plan.domain.usecase.AdvanceActivePlanDayUseCase
import io.aethibo.combatcoach.shared.plan.domain.usecase.ClearActivePlanUseCase
import io.aethibo.combatcoach.shared.plan.domain.usecase.ObserveActivePlanUseCase
import io.aethibo.combatcoach.shared.plan.domain.usecase.ObservePlanByIdUseCase
import io.aethibo.combatcoach.shared.plan.domain.usecase.SetActivePlanUseCase
import io.aethibo.combatcoach.shared.utils.ItemType
import io.aethibo.combatcoach.shared.workout.domain.usecase.ObserveWorkoutsUseCase
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

private fun buildWeekGroups(
    plan: Plan?,
    activePlan: ActivePlan?,
    expandedWeeks: Set<Int>,
): List<WeekGroup> {
    val days = plan?.days ?: return emptyList()
    val currentDayIdx = (activePlan?.currentDay ?: 1) - 1
    val currentWeek = (currentDayIdx / 7) + 1

    return days.chunked(7).mapIndexed { weekIdx, weekDays ->
        val weekNum = weekIdx + 1
        val completedInWeek = weekDays.count { day -> (day.dayNumber - 1) < currentDayIdx }
        WeekGroup(
            weekNumber = weekNum,
            label = "Week $weekNum",
            days = weekDays,
            isExpanded = weekNum in expandedWeeks,
            isCurrentWeek = weekNum == currentWeek,
            completedCount = completedInWeek,
        )
    }
}

@Composable
fun planDetailPresenter(
    planId: Int,
    observePlanById: ObservePlanByIdUseCase,
    observeWorkouts: ObserveWorkoutsUseCase,
    observeCombos: ObserveCombosUseCase,
    observeActivePlan: ObserveActivePlanUseCase,
    setActivePlan: SetActivePlanUseCase,
    clearActivePlan: ClearActivePlanUseCase,
    advanceActivePlanDay: AdvanceActivePlanDayUseCase,
    onNavigateToTimer: (ItemType, Int) -> Unit,
    onNavigateToEdit: (Int) -> Unit,
): PlanDetailState {

    val scope = rememberCoroutineScope()

    // UI-only state — not from flows
    var expandedWeeks by remember { mutableStateOf<Set<Int>>(emptySet()) }
    var showStartSheet by remember { mutableStateOf(false) }
    var sheetDayIndex by remember { mutableIntStateOf(0) }

    // Single combined flow — keyed on planId
    val data by remember(planId) {
        combine(
            observePlanById(planId),
            observeWorkouts(),
            observeCombos(),
            observeActivePlan(),
        ) { plan, workouts, combos, active ->
            PlanDetailData(plan, workouts, combos, active)
        }
    }.collectAsState(initial = PlanDetailData())

    LaunchedEffect(data.plan) {
        val plan = data.plan ?: return@LaunchedEffect
        if (plan.totalDays > WEEK_VIEW_THRESHOLD) {
            val currentDayIdx = (data.activePlan?.currentDay ?: 1) - 1
            val currentWeek = (currentDayIdx / 7) + 1
            expandedWeeks = setOf(currentWeek)
        }
    }

    // Derived maps — recomputed only when their inputs change
    val workoutMap = remember(data.workouts) { data.workouts.associateBy { it.id } }
    val comboMap = remember(data.combos) { data.combos.associateBy { it.id } }

    val useWeekView = (data.plan?.totalDays ?: 0) > WEEK_VIEW_THRESHOLD

    val weekGroups = remember(data.plan, data.activePlan, expandedWeeks) {
        buildWeekGroups(data.plan, data.activePlan, expandedWeeks)
    }

    val isActivePlan = data.activePlan?.planId == planId

    val eventSink: (PlanDetailEvent) -> Unit = remember {
        { event ->
            when (event) {
                PlanDetailEvent.SetAsActive -> scope.launch { setActivePlan(planId) }
                PlanDetailEvent.DeactivatePlan -> scope.launch { clearActivePlan() }
                PlanDetailEvent.AdvanceDay -> scope.launch { advanceActivePlanDay() }

                is PlanDetailEvent.OpenStartSheet -> {
                    sheetDayIndex = event.dayIndex
                    showStartSheet = true
                }

                PlanDetailEvent.DismissStartSheet -> showStartSheet = false

                is PlanDetailEvent.StartSession -> {
                    showStartSheet = false
                    when (val session = event.session) {
                        is SessionType.Workout -> onNavigateToTimer(
                            ItemType.Workout,
                            session.id
                        )

                        is SessionType.Combo -> onNavigateToTimer(
                            ItemType.Combo,
                            session.id
                        )
                    }
                }

                is PlanDetailEvent.ToggleWeek -> {
                    expandedWeeks = if (event.weekNumber in expandedWeeks)
                        expandedWeeks - event.weekNumber
                    else
                        expandedWeeks + event.weekNumber
                }

                PlanDetailEvent.Edit -> onNavigateToEdit(planId)
            }
        }
    }

    return PlanDetailState(
        plan = data.plan,
        activePlan = data.activePlan,
        workouts = workoutMap,
        combos = comboMap,
        isActivePlan = isActivePlan,
        isLoading = data.plan == null,
        useWeekView = useWeekView,
        weekGroups = weekGroups,
        expandedWeeks = expandedWeeks,
        showStartSheet = showStartSheet,
        sheetDayIndex = sheetDayIndex,
        eventSink = eventSink,
    )
}
