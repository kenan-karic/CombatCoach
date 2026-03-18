package io.aethibo.combatcoach.features.create.plan

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import io.aethibo.combatcoach.core.failure.Failure
import io.aethibo.combatcoach.features.create.utils.CreateEditMode
import io.aethibo.combatcoach.features.create.utils.CreateEditMode.Edit
import io.aethibo.combatcoach.shared.combo.domain.model.Combo
import io.aethibo.combatcoach.shared.combo.domain.usecase.ObserveCombosUseCase
import io.aethibo.combatcoach.shared.plan.domain.failure.PlanFailure
import io.aethibo.combatcoach.shared.plan.domain.model.Plan
import io.aethibo.combatcoach.shared.plan.domain.model.PlanDay
import io.aethibo.combatcoach.shared.plan.domain.model.PlanType
import io.aethibo.combatcoach.shared.plan.domain.usecase.DeletePlanUseCase
import io.aethibo.combatcoach.shared.plan.domain.usecase.ObservePlanByIdUseCase
import io.aethibo.combatcoach.shared.plan.domain.usecase.SavePlanUseCase
import io.aethibo.combatcoach.shared.utils.Discipline
import io.aethibo.combatcoach.shared.workout.domain.model.Workout
import io.aethibo.combatcoach.shared.workout.domain.usecase.ObserveWorkoutsUseCase
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private data class PlanResources(
    val workouts: List<Workout> = emptyList(),
    val combos: List<Combo> = emptyList(),
)

@Composable
fun planEditPresenter(
    mode: CreateEditMode,
    observePlanById: ObservePlanByIdUseCase,
    observeWorkouts: ObserveWorkoutsUseCase,
    observeCombos: ObserveCombosUseCase,
    savePlan: SavePlanUseCase,
    deletePlan: DeletePlanUseCase,
    onSaved: () -> Unit,
    onDeleted: () -> Unit,
): PlanEditState {

    val scope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var discipline by remember { mutableStateOf(Discipline.GENERAL) }
    var planType by remember { mutableStateOf(PlanType.PROGRAM) }
    var days by remember { mutableStateOf<List<PlanDay>>(emptyList()) }
    var collectionIds by remember { mutableStateOf<List<Int>>(emptyList()) }
    var isLoading by remember { mutableStateOf(mode is Edit) }
    var isSaving by remember { mutableStateOf(false) }
    var nameError by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var expandedDayIndex by remember { mutableStateOf<Int?>(null) }

    val resources by remember {
        combine(observeWorkouts(), observeCombos()) { workouts, combos ->
            PlanResources(workouts, combos)
        }
    }.collectAsState(initial = PlanResources())

    if (mode is Edit) {
        LaunchedEffect(mode.id) {
            val plan = observePlanById(mode.id).filterNotNull().first()
            name = plan.name
            description = plan.description
            discipline = plan.discipline
            planType = plan.planType
            days = plan.days
            collectionIds = plan.workoutIds
            isLoading = false
        }
    }

    fun validate(): Boolean {
        nameError = if (name.isBlank()) "Name is required" else null
        return nameError == null
    }

    val eventSink: (PlanEditEvent) -> Unit = remember {
        { event ->
            when (event) {
                is PlanEditEvent.NameChanged -> {
                    name = event.value; nameError = null
                }

                is PlanEditEvent.DescriptionChanged -> description = event.value
                is PlanEditEvent.DisciplineChanged -> discipline = event.value
                is PlanEditEvent.TypeChanged -> planType = event.value

                PlanEditEvent.AddDay -> {
                    days = days + PlanDay(dayNumber = days.size + 1)
                }

                is PlanEditEvent.RemoveDay -> {
                    days = days.toMutableList()
                        .also { it.removeAt(event.index) }
                        .mapIndexed { i, d -> d.copy(dayNumber = i + 1) }
                }

                is PlanEditEvent.UpdateDayLabel -> {
                    days = days.mapIndexed { i, d ->
                        if (i == event.index) d.copy(label = event.label) else d
                    }
                }

                is PlanEditEvent.AddWorkoutToDay -> {
                    days = days.mapIndexed { i, d ->
                        if (i == event.dayIndex && event.workoutId !in d.workoutIds)
                            d.copy(workoutIds = d.workoutIds + event.workoutId) else d
                    }
                }

                is PlanEditEvent.RemoveWorkoutFromDay -> {
                    days = days.mapIndexed { i, d ->
                        if (i == event.dayIndex)
                            d.copy(workoutIds = d.workoutIds.filter { it != event.workoutId }) else d
                    }
                }

                is PlanEditEvent.AddComboToDay -> {
                    days = days.mapIndexed { i, d ->
                        if (i == event.dayIndex && event.comboId !in d.comboIds)
                            d.copy(comboIds = d.comboIds + event.comboId) else d
                    }
                }

                is PlanEditEvent.RemoveComboFromDay -> {
                    days = days.mapIndexed { i, d ->
                        if (i == event.dayIndex)
                            d.copy(comboIds = d.comboIds.filter { it != event.comboId }) else d
                    }
                }

                is PlanEditEvent.ToggleDayRest -> {
                    days = days.mapIndexed { i, d ->
                        if (i == event.dayIndex) d.copy(isRestDay = !d.isRestDay) else d
                    }
                }

                is PlanEditEvent.ToggleExpandDay -> {
                    expandedDayIndex = if (expandedDayIndex == event.index) null else event.index
                }

                is PlanEditEvent.AddToCollection -> {
                    if (event.workoutId !in collectionIds) collectionIds =
                        collectionIds + event.workoutId
                }

                is PlanEditEvent.RemoveFromCollection -> {
                    collectionIds = collectionIds.filter { it != event.workoutId }
                }

                PlanEditEvent.Save -> {
                    if (!validate()) return@remember
                    scope.launch {
                        isSaving = true
                        errorMessage = null

                        val plan = Plan(
                            id = if (mode is Edit) mode.id else 0,
                            name = name.trim(),
                            description = description.trim(),
                            discipline = discipline,
                            planType = planType,
                            days = days,
                            workoutIds = collectionIds,
                        )

                        savePlan(plan).fold(
                            ifLeft = { failure ->
                                isSaving = false; errorMessage = failure.toPlanUserMessage()
                            },
                            ifRight = { isSaving = false; onSaved() }
                        )
                    }
                }

                PlanEditEvent.Delete -> showDeleteDialog = true
                PlanEditEvent.DismissDelete -> showDeleteDialog = false
                PlanEditEvent.ConfirmDelete -> {
                    scope.launch {
                        if (mode is Edit) {
                            deletePlan(mode.id).fold(
                                ifLeft = { failure -> errorMessage = failure.toPlanUserMessage() },
                                ifRight = { onDeleted() }
                            )
                        }
                    }
                }
            }
        }
    }

    return PlanEditState(
        mode = mode,
        name = name,
        description = description,
        discipline = discipline,
        planType = planType,
        days = days,
        collectionWorkoutIds = collectionIds,
        availableWorkouts = resources.workouts,
        availableCombos = resources.combos,
        isLoading = isLoading,
        isSaving = isSaving,
        nameError = nameError,
        errorMessage = errorMessage,
        showDeleteDialog = showDeleteDialog,
        expandedDayIndex = expandedDayIndex,
        eventSink = eventSink,
    )
}

internal fun Failure.toPlanUserMessage(): String = when (this) {
    is PlanFailure.NotFound -> "Plan #$id no longer exists."
    is PlanFailure.InvalidData -> "\"$field\" cannot be blank."
    is PlanFailure.PersistenceFailed -> "Could not save — please try again."
    is PlanFailure.SerializationFailed -> "Plan data is corrupted. Please recreate the plan."
    is PlanFailure.NoActivePlan -> "No active plan found."
    is Failure.Unknown -> "An unexpected error occurred."
    else -> "An unexpected error occurred."
}
