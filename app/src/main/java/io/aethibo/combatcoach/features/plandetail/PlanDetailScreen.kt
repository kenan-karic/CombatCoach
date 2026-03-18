package io.aethibo.combatcoach.features.plandetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import io.aethibo.combatcoach.core.ui.components.SectionHeader
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing
import io.aethibo.combatcoach.features.plandetail.components.CollectionView
import io.aethibo.combatcoach.features.plandetail.components.DayCard
import io.aethibo.combatcoach.features.plandetail.components.PlanBottomBar
import io.aethibo.combatcoach.features.plandetail.components.PlanDetailHeader
import io.aethibo.combatcoach.features.plandetail.components.PlanProgressCard
import io.aethibo.combatcoach.features.plandetail.components.StartSessionSheet
import io.aethibo.combatcoach.features.plandetail.components.WeekSection
import io.aethibo.combatcoach.features.plandetail.model.SessionType
import io.aethibo.combatcoach.shared.plan.domain.model.PlanType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanDetailScreen(
    state: PlanDetailState,
    onBack: () -> Unit,
) {
    val sp = LocalSpacing.current

    if (state.isLoading || state.plan == null) {
        Box(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            Alignment.Center,
        ) { CircularProgressIndicator(color = MaterialTheme.colorScheme.primary) }
        return
    }

    val plan = state.plan

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        plan.name,
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { state.eventSink(PlanDetailEvent.Edit) }) {
                        Icon(Icons.Outlined.Edit, "Edit plan")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
            )
        },
        bottomBar = { PlanBottomBar(state = state) },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = sp.xxl),
        ) {
            item { PlanDetailHeader(plan = plan, state = state) }

            if (state.isActivePlan && state.activePlan != null) {
                item {
                    PlanProgressCard(
                        state = state,
                        modifier = Modifier.padding(horizontal = sp.screenPadding),
                    )
                    Spacer(Modifier.height(sp.sectionGap))
                }
            }

            when {
                plan.planType == PlanType.COLLECTION -> {
                    item {
                        SectionHeader(
                            title = "Workouts in Collection (${plan.workoutIds.size})",
                            modifier = Modifier.padding(horizontal = sp.screenPadding),
                        )
                        Spacer(Modifier.height(sp.small))
                    }
                    item {
                        CollectionView(
                            workoutIds = plan.workoutIds,
                            workouts = state.workouts,
                            currentIdx = state.currentDayIndex,
                            isActive = state.isActivePlan,
                            onStart = { id ->
                                state.eventSink(PlanDetailEvent.StartSession(SessionType.Workout(id)))
                            },
                            modifier = Modifier.padding(horizontal = sp.screenPadding),
                        )
                    }
                }

                state.useWeekView -> {
                    item {
                        SectionHeader(
                            title = "${plan.days.size} days · ${state.weekGroups.size} weeks",
                            modifier = Modifier.padding(horizontal = sp.screenPadding),
                        )
                        Spacer(Modifier.height(sp.small))
                    }
                    items(items = state.weekGroups, key = { "week-${it.weekNumber}" }) { group ->
                        WeekSection(
                            group = group,
                            currentDayIdx = state.currentDayIndex,
                            isActivePlan = state.isActivePlan,
                            workouts = state.workouts,
                            combos = state.combos,
                            onToggle = { state.eventSink(PlanDetailEvent.ToggleWeek(group.weekNumber)) },
                            onOpenSession = { dayIdx ->
                                state.eventSink(
                                    PlanDetailEvent.OpenStartSheet(
                                        dayIdx
                                    )
                                )
                            },
                            modifier = Modifier.padding(horizontal = sp.screenPadding),
                        )
                        Spacer(Modifier.height(sp.cardGap))
                    }
                }

                else -> {
                    item {
                        SectionHeader(
                            title = "${plan.days.size} days",
                            modifier = Modifier.padding(horizontal = sp.screenPadding),
                        )
                        Spacer(Modifier.height(sp.small))
                    }
                    itemsIndexed(
                        items = plan.days,
                        key = { _, d -> "day-${d.dayNumber}" }) { index, day ->
                        DayCard(
                            day = day,
                            dayIndex = index,
                            currentDayIdx = state.currentDayIndex,
                            isActivePlan = state.isActivePlan,
                            workouts = state.workouts,
                            combos = state.combos,
                            onOpenSession = { state.eventSink(PlanDetailEvent.OpenStartSheet(index)) },
                            modifier = Modifier.padding(horizontal = sp.screenPadding),
                        )
                        Spacer(Modifier.height(sp.cardGap))
                    }
                }
            }
        }
    }

    if (state.showStartSheet) {
        val day = state.plan.days.getOrNull(state.sheetDayIndex)
        if (day != null) {
            StartSessionSheet(
                day = day,
                workouts = state.workouts,
                combos = state.combos,
                onStart = { session -> state.eventSink(PlanDetailEvent.StartSession(session)) },
                onDismiss = { state.eventSink(PlanDetailEvent.DismissStartSheet) },
            )
        }
    }
}
