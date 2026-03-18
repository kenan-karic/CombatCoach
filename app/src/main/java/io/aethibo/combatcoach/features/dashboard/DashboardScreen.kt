package io.aethibo.combatcoach.features.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.aethibo.combatcoach.R
import io.aethibo.combatcoach.core.ui.components.SectionHeader
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing
import io.aethibo.combatcoach.features.dashboard.components.ActivePlanCard
import io.aethibo.combatcoach.features.dashboard.components.ComboCard
import io.aethibo.combatcoach.features.dashboard.components.DashboardEmptyState
import io.aethibo.combatcoach.features.dashboard.components.DashboardHeader
import io.aethibo.combatcoach.features.dashboard.components.StatsStrip
import io.aethibo.combatcoach.features.dashboard.components.WorkoutCard
import io.aethibo.combatcoach.shared.combo.domain.model.Combo
import io.aethibo.combatcoach.shared.log.domain.model.DashboardStats
import io.aethibo.combatcoach.shared.plan.domain.model.ActivePlan
import io.aethibo.combatcoach.shared.plan.domain.model.Plan
import io.aethibo.combatcoach.shared.plan.domain.utils.PlanProgress
import io.aethibo.combatcoach.shared.utils.Discipline
import io.aethibo.combatcoach.shared.utils.WorkoutDiscipline
import io.aethibo.combatcoach.shared.workout.domain.model.Workout
import io.aethibo.combatcoach.shared.workout.domain.model.WorkoutType

@Composable
fun DashboardScreen(state: DashboardState) {
    val sp = LocalSpacing.current

    if (state.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
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
        // ── Header ─────────────────────────────────────────────────────────
        item {
            DashboardHeader(
                greeting = state.greeting,
                today = state.today,
                onCreateWorkout = { state.eventSink(DashboardEvent.CreateWorkout) },
                onCreateCombo = { state.eventSink(DashboardEvent.CreateCombo) },
                onCreatePlan = { state.eventSink(DashboardEvent.CreatePlan) },
            )
        }

        // ── Stats strip ────────────────────────────────────────────────────
        item {
            StatsStrip(
                stats = state.stats,
                modifier = Modifier.padding(horizontal = sp.screenPadding),
            )
            Spacer(Modifier.height(sp.sectionGap))
        }

        // ── Active plan card ───────────────────────────────────────────────
        state.activePlanProgress?.let { progress ->
            item {
                SectionHeader(
                    title = "Active Plan",
                    modifier = Modifier.padding(horizontal = sp.screenPadding),
                )
                Spacer(Modifier.height(sp.small))
                ActivePlanCard(
                    progress = progress,
                    onClick = { state.eventSink(DashboardEvent.OpenPlan(progress.plan.id)) },
                    modifier = Modifier.padding(horizontal = sp.screenPadding),
                )
                Spacer(Modifier.height(sp.sectionGap))
            }
        }

        // ── Today's session ────────────────────────────────────────────────
        if (state.hasTodayContent) {
            item {
                SectionHeader(
                    title = state.todaySectionTitle,
                    modifier = Modifier.padding(horizontal = sp.screenPadding),
                )
                Spacer(Modifier.height(sp.small))
            }

            items(state.todayWorkouts, key = { "w-${it.id}" }) { workout ->
                WorkoutCard(
                    workout = workout,
                    onStart = { state.eventSink(DashboardEvent.StartWorkout(workout.id)) },
                    onOpen = { state.eventSink(DashboardEvent.OpenWorkout(workout.id)) },
                    modifier = Modifier.padding(horizontal = sp.screenPadding),
                )
                Spacer(Modifier.height(sp.cardGap))
            }

            items(state.todayCombos, key = { "c-${it.id}" }) { combo ->
                ComboCard(
                    combo = combo,
                    onStart = { state.eventSink(DashboardEvent.StartCombo(combo.id)) },
                    onOpen = { state.eventSink(DashboardEvent.OpenCombo(combo.id)) },
                    modifier = Modifier.padding(horizontal = sp.screenPadding),
                )
                Spacer(Modifier.height(sp.cardGap))
            }

            item { Spacer(Modifier.height(sp.sectionGap)) }
        }

        // ── Recent workouts ────────────────────────────────────────────────
        if (state.recentWorkouts.isNotEmpty()) {
            item {
                SectionHeader(
                    title = "Recent Workouts",
                    modifier = Modifier.padding(horizontal = sp.screenPadding),
                )
                Spacer(Modifier.height(sp.small))
            }
            items(state.recentWorkouts, key = { "rw-${it.id}" }) { workout ->
                WorkoutCard(
                    workout = workout,
                    onStart = { state.eventSink(DashboardEvent.StartWorkout(workout.id)) },
                    onOpen = { state.eventSink(DashboardEvent.OpenWorkout(workout.id)) },
                    modifier = Modifier.padding(horizontal = sp.screenPadding),
                )
                Spacer(Modifier.height(sp.cardGap))
            }
            item { Spacer(Modifier.height(sp.sectionGap)) }
        }

        // ── Recent combos ──────────────────────────────────────────────────
        if (state.recentCombos.isNotEmpty()) {
            item {
                SectionHeader(
                    title = "Recent Combos",
                    modifier = Modifier.padding(horizontal = sp.screenPadding),
                )
                Spacer(Modifier.height(sp.small))
            }
            items(state.recentCombos, key = { "rc-${it.id}" }) { combo ->
                ComboCard(
                    combo = combo,
                    onStart = { state.eventSink(DashboardEvent.StartCombo(combo.id)) },
                    onOpen = { state.eventSink(DashboardEvent.OpenCombo(combo.id)) },
                    modifier = Modifier.padding(horizontal = sp.screenPadding),
                )
                Spacer(Modifier.height(sp.cardGap))
            }
            item { Spacer(Modifier.height(sp.sectionGap)) }
        }

        // ── Empty state ────────────────────────────────────────────────────
        if (state.isEmpty) {
            item {
                DashboardEmptyState(
                    onCreateWorkout = { state.eventSink(DashboardEvent.CreateWorkout) },
                    onCreateCombo = { state.eventSink(DashboardEvent.CreateCombo) },
                    modifier = Modifier.padding(sp.screenPadding),
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Loading")
@Composable
private fun DashboardLoadingPreview() {
    CombatCoachTheme {
        DashboardScreen(
            state = DashboardState(
                isLoading = true
            )
        )
    }
}

@Preview(showBackground = true, name = "Empty state")
@Composable
private fun DashboardEmptyPreview() {
    CombatCoachTheme {
        val previewStats = DashboardStats(
            totalWorkouts = 24,
            workoutsThisWeek = 3,
            currentStreak = 5,
            totalMinutes = 820,
        )

        DashboardScreen(
            state = DashboardState(
                isLoading = false,
                greeting = R.string.dashboard_greeting_morning,
                today = "Wednesday, February 25",
                stats = previewStats,
            )
        )
    }
}

@Preview(showBackground = true, name = "With plan and today's session")
@Composable
private fun DashboardWithPlanPreview() {
    CombatCoachTheme {
        val previewStats = DashboardStats(
            totalWorkouts = 24,
            workoutsThisWeek = 3,
            currentStreak = 5,
            totalMinutes = 820,
        )

        val previewPlan = Plan(
            id = 1,
            name = "8-Week Boxing Program",
        )

        val previewPlanProgress = PlanProgress(
            plan = previewPlan,
            activePlan = ActivePlan(planId = 1, currentDay = 15),
            completedDays = 14,
            totalDays = 56,
        )

        val previewWorkout = Workout(
            id = 1,
            name = "Boxing Fundamentals",
            workoutDiscipline = WorkoutDiscipline.STRENGTH,
            type = WorkoutType.CIRCUIT,
            estimatedDurationMinutes = 45,
        )

        val previewCombo = Combo(
            id = 1,
            name = "Jab-Cross-Hook",
            discipline = Discipline.STRIKING,
            rounds = 5,
            durationSeconds = 30,
        )

        DashboardScreen(
            state = DashboardState(
                isLoading = false,
                greeting = R.string.dashboard_greeting_afternoon,
                today = "Wednesday, February 25",
                stats = previewStats,
                activePlanProgress = previewPlanProgress,
                todayWorkouts = listOf(previewWorkout),
                todayCombos = listOf(previewCombo),
            )
        )
    }
}

@Preview(showBackground = true, name = "Recent only — no plan")
@Composable
private fun DashboardRecentOnlyPreview() {
    CombatCoachTheme {
        val previewStats = DashboardStats(
            totalWorkouts = 24,
            workoutsThisWeek = 3,
            currentStreak = 5,
            totalMinutes = 820,
        )

        val previewWorkout = Workout(
            id = 1,
            name = "Boxing Fundamentals",
            workoutDiscipline = WorkoutDiscipline.MMA_CONDITIONING,
            type = WorkoutType.CIRCUIT,
            estimatedDurationMinutes = 45,
        )

        val previewCombo = Combo(
            id = 1,
            name = "Jab-Cross-Hook",
            discipline = Discipline.STRIKING,
            rounds = 5,
            durationSeconds = 30,
        )

        DashboardScreen(
            state = DashboardState(
                isLoading = false,
                greeting = R.string.dashboard_greeting_evening,
                today = "Wednesday, February 25",
                stats = previewStats,
                recentWorkouts = listOf(
                    previewWorkout,
                    previewWorkout.copy(id = 2, name = "Strength Session")
                ),
                recentCombos = listOf(previewCombo),
            )
        )
    }
}
