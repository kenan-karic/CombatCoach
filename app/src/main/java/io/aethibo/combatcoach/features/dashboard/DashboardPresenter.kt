package io.aethibo.combatcoach.features.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import io.aethibo.combatcoach.R
import io.aethibo.combatcoach.features.dashboard.model.DashboardData
import io.aethibo.combatcoachex.features.editcreate.presentation.model.ItemType
import io.aethibo.combatcoachex.features.shared.combo.domain.model.Combo
import io.aethibo.combatcoachex.features.shared.combo.domain.usease.ObserveCombosUseCase
import io.aethibo.combatcoachex.features.shared.log.domain.model.DashboardStats
import io.aethibo.combatcoachex.features.shared.log.domain.usecase.ObserveDashboardStatsUseCase
import io.aethibo.combatcoachex.features.shared.plan.domain.model.ActivePlan
import io.aethibo.combatcoachex.features.shared.plan.domain.model.Plan
import io.aethibo.combatcoachex.features.shared.plan.domain.model.PlanType
import io.aethibo.combatcoachex.features.shared.plan.domain.usecase.ObserveActivePlanUseCase
import io.aethibo.combatcoachex.features.shared.plan.domain.usecase.ObservePlansUseCase
import io.aethibo.combatcoachex.features.shared.plan.domain.utils.PlanProgress
import io.aethibo.combatcoachex.features.shared.workout.domain.model.Workout
import io.aethibo.combatcoachex.features.shared.workout.domain.usecase.ObserveWorkoutsUseCase
import kotlinx.coroutines.flow.combine
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.collections.filter
import kotlin.collections.find
import kotlin.collections.take

@Composable
fun dashboardPresenter(
    observeWorkouts: ObserveWorkoutsUseCase,
    observeCombos: ObserveCombosUseCase,
    observePlans: ObservePlansUseCase,
    observeActivePlan: ObserveActivePlanUseCase,
    observeDashboardStats: ObserveDashboardStatsUseCase,
    onNavigateToCreate: (ItemType) -> Unit,
    onNavigateToWorkout: (Int) -> Unit,
    onNavigateToCombo: (Int) -> Unit,
    onNavigateToPlan: (Int) -> Unit,
    onNavigateToTimer: (ItemType, Int) -> Unit,
): DashboardState {

    // ── Combined flow → single collectAsState ──────────────────────────────
    // WHY not LaunchedEffect + five var assignments?
    //
    // LaunchedEffect with five separate `var x by remember { }` assignments
    // inside the collector causes up to five sequential recompositions per
    // emission — one per assignment. Each assignment triggers Compose's
    // snapshot system independently.
    //
    // collectAsState on a single combined Flow produces one StateFlow emission
    // → one recomposition. Atomic, correct, consistent with the pattern used
    // across the rest of this architecture.
    val data by remember {
        combine(
            observeWorkouts(),
            observeCombos(),
            observePlans(),
            observeActivePlan(),
            observeDashboardStats(),
        ) { workouts, combos, plans, activePlan, stats ->
            DashboardData(workouts, combos, plans, activePlan, stats)
        }
    }.collectAsState(initial = DashboardData())

    // ── Derived state — recomputed only when inputs change ─────────────────

    val activePlanDetail = remember(data.activePlan, data.plans) {
        data.activePlan?.let { ap -> data.plans.find { it.id == ap.planId } }
    }

    val todayWorkouts = remember(data.activePlan, activePlanDetail, data.workouts) {
        resolveTodayWorkouts(data.activePlan, activePlanDetail, data.workouts)
    }

    val todayCombos = remember(data.activePlan, activePlanDetail, data.combos) {
        resolveTodayCombos(data.activePlan, activePlanDetail, data.combos)
    }

    val recentWorkouts = remember(data.workouts, todayWorkouts) {
        data.workouts
            .filter { w -> todayWorkouts.none { it.id == w.id } }
            .take(5)
    }

    val recentCombos = remember(data.combos, todayCombos) {
        data.combos
            .filter { c -> todayCombos.none { it.id == c.id } }
            .take(5)
    }

    val activePlanProgress = remember(data.activePlan, activePlanDetail) {
        val ap = data.activePlan ?: return@remember null
        val plan = activePlanDetail ?: return@remember null
        PlanProgress(
            plan = plan,
            activePlan = ap,
            completedDays = ap.currentDay - 1,
            totalDays = plan.totalDays,
        )
    }

    // ── Static values — computed once ──────────────────────────────────────
    val greeting = remember { buildGreeting() }

    val today = remember {
        SimpleDateFormat("EEEE, MMMM d", Locale.getDefault()).format(Date())
    }

    val eventSink: (DashboardEvent) -> Unit = remember {
        { event ->
            when (event) {
                is DashboardEvent.StartWorkout -> onNavigateToTimer(ItemType.Workout, event.id)
                is DashboardEvent.StartCombo -> onNavigateToTimer(ItemType.Combo, event.id)
                is DashboardEvent.OpenWorkout -> onNavigateToWorkout(event.id)
                is DashboardEvent.OpenPlan -> onNavigateToPlan(event.id)
                DashboardEvent.CreateWorkout -> onNavigateToCreate(ItemType.Workout)
                DashboardEvent.CreateCombo -> onNavigateToCreate(ItemType.Combo)
                DashboardEvent.CreatePlan -> onNavigateToCreate(ItemType.Plan)
                is DashboardEvent.OpenCombo -> onNavigateToCombo(event.id)
            }
        }
    }

    return DashboardState(
        greeting = greeting,
        today = today,
        stats = data.stats,
        activePlanProgress = activePlanProgress,
        todayWorkouts = todayWorkouts,
        todayCombos = todayCombos,
        recentWorkouts = recentWorkouts,
        recentCombos = recentCombos,
        isLoading = false,
        eventSink = eventSink,
    )
}

private fun resolveTodayWorkouts(
    activePlan: ActivePlan?,
    plan: Plan?,
    allWorkouts: List<Workout>,
): List<Workout> {
    if (activePlan == null || plan == null) return allWorkouts.take(3)
    return when (plan.planType) {
        PlanType.PROGRAM -> {
            val day = plan.days.getOrNull(activePlan.currentDay - 1) ?: return emptyList()
            allWorkouts.filter { it.id in day.workoutIds }
        }

        PlanType.COLLECTION -> {
            val idx = (activePlan.currentDay - 1) % plan.workoutIds.size
            allWorkouts.filter { it.id == plan.workoutIds.getOrNull(idx) }
        }
    }
}

private fun resolveTodayCombos(
    activePlan: ActivePlan?,
    plan: Plan?,
    allCombos: List<Combo>,
): List<Combo> {
    if (activePlan == null || plan == null) return allCombos.take(3)
    if (plan.planType != PlanType.PROGRAM) return emptyList()
    val day = plan.days.getOrNull(activePlan.currentDay - 1) ?: return emptyList()
    return allCombos.filter { it.id in day.comboIds }
}

private fun buildGreeting(): String {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return when {
        hour < 12 -> R.string.dashboard_greeting_morning
        hour < 17 ->  R.string.dashboard_greeting_afternoon
        else -> R.string.dashboard_greeting_evening
    }
}
