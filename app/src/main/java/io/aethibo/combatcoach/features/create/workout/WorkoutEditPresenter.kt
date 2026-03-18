package io.aethibo.combatcoach.features.create.workout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import io.aethibo.combatcoach.core.failure.Failure
import io.aethibo.combatcoach.features.create.utils.CreateEditMode
import io.aethibo.combatcoach.shared.utils.ItemType
import io.aethibo.combatcoach.shared.utils.WorkoutDiscipline
import io.aethibo.combatcoach.shared.workout.domain.failure.WorkoutFailure
import io.aethibo.combatcoach.shared.workout.domain.model.Exercise
import io.aethibo.combatcoach.shared.workout.domain.model.Workout
import io.aethibo.combatcoach.shared.workout.domain.model.WorkoutType
import io.aethibo.combatcoach.shared.workout.domain.model.asCircuitDefault
import io.aethibo.combatcoach.shared.workout.domain.model.asStrengthDefault
import io.aethibo.combatcoach.shared.workout.domain.usecase.DeleteWorkoutUseCase
import io.aethibo.combatcoach.shared.workout.domain.usecase.ObserveWorkoutByIdUseCase
import io.aethibo.combatcoach.shared.workout.domain.usecase.SaveWorkoutUseCase
import io.aethibo.combatcoach.shared.workout.domain.utils.estimatedDurationMinutes
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun workoutEditPresenter(
    mode: CreateEditMode,
    observeWorkoutById: ObserveWorkoutByIdUseCase,
    saveWorkout: SaveWorkoutUseCase,
    deleteWorkout: DeleteWorkoutUseCase,
    onSaved: () -> Unit,
    onDeleted: () -> Unit,
    onBack: () -> Unit,
    onStartTimer: ((ItemType, Int) -> Unit)? = null,
): WorkoutEditState {

    val scope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var workoutDiscipline by remember { mutableStateOf(WorkoutDiscipline.STRENGTH) }
    var workoutType by remember { mutableStateOf(WorkoutType.STRENGTH) }
    var circuitRounds by remember { mutableIntStateOf(3) }
    var circuitRest by remember { mutableIntStateOf(60) }
    var exercises by remember { mutableStateOf<List<Exercise>>(emptyList()) }
    var isLoading by remember { mutableStateOf(mode is CreateEditMode.Edit) }
    var isDirty by remember { mutableStateOf(false) }
    var isSaving by remember { mutableStateOf(false) }
    var nameError by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showDiscardDialog by remember { mutableStateOf(false) }

    if (mode is CreateEditMode.Edit) {
        LaunchedEffect(mode.id) {
            val workout = observeWorkoutById(mode.id).filterNotNull().first()
            name = workout.name
            description = workout.description
            workoutDiscipline = workout.workoutDiscipline
            workoutType = workout.type
            circuitRounds = workout.circuitRounds
            circuitRest = workout.circuitRestBetweenRoundsSeconds
            exercises = workout.exercises.sortedBy { it.orderIndex }
            isLoading = false
            isDirty = false
        }
    }

    fun validate(): Boolean {
        nameError = if (name.isBlank()) "Name is required" else null
        return nameError == null
    }

    val eventSink: (WorkoutEditEvent) -> Unit = remember {
        { event ->
            when (event) {
                is WorkoutEditEvent.NameChanged -> {
                    name = event.value; nameError = null; isDirty = true
                }

                is WorkoutEditEvent.DescriptionChanged -> {
                    description = event.value; isDirty = true
                }

                is WorkoutEditEvent.WorkoutDisciplineChanged -> {
                    // event.value is TrainingDiscipline (the interface type on the event).
                    // Cast is safe: WorkoutDisciplineSelector only ever emits WorkoutDiscipline values.
                    workoutDiscipline = event.value as WorkoutDiscipline
                    isDirty = true
                }

                is WorkoutEditEvent.TypeChanged -> {
                    workoutType = event.value
                    exercises = when (event.value) {
                        WorkoutType.CIRCUIT -> exercises.map { it.asCircuitDefault() }
                        WorkoutType.STRENGTH -> exercises.map { it.asStrengthDefault() }
                    }
                    isDirty = true
                }

                is WorkoutEditEvent.CircuitRoundsChanged -> {
                    circuitRounds = event.rounds.coerceAtLeast(1); isDirty = true
                }

                is WorkoutEditEvent.CircuitRestChanged -> {
                    circuitRest = event.seconds; isDirty = true
                }

                is WorkoutEditEvent.AddExercise -> {
                    val normalised = when (workoutType) {
                        WorkoutType.CIRCUIT -> event.exercise.asCircuitDefault()
                        WorkoutType.STRENGTH -> event.exercise.asStrengthDefault()
                    }
                    exercises = exercises + normalised.copy(orderIndex = exercises.size)
                    isDirty = true
                }

                is WorkoutEditEvent.UpdateExercise -> {
                    exercises =
                        exercises.map { if (it.id == event.exercise.id) event.exercise else it }
                    isDirty = true
                }

                is WorkoutEditEvent.RemoveExercise -> {
                    exercises = exercises
                        .filter { it.id != event.id }
                        .mapIndexed { i, ex -> ex.copy(orderIndex = i) }
                    isDirty = true
                }

                is WorkoutEditEvent.ReorderExercises -> {
                    val from = event.from
                    val to = event.to
                    if (from in exercises.indices && to in exercises.indices) {
                        val mutable = exercises.toMutableList()
                        mutable.add(to, mutable.removeAt(from))
                        exercises = mutable.mapIndexed { i, ex -> ex.copy(orderIndex = i) }
                        isDirty = true
                    }
                }

                WorkoutEditEvent.Save -> {
                    if (!validate()) return@remember
                    scope.launch {
                        isSaving = true
                        errorMessage = null
                        saveWorkout(
                            Workout(
                                id = if (mode is CreateEditMode.Edit) mode.id else 0,
                                name = name.trim(),
                                description = description.trim(),
                                workoutDiscipline = workoutDiscipline,
                                type = workoutType,
                                circuitRounds = circuitRounds,
                                circuitRestBetweenRoundsSeconds = circuitRest,
                                exercises = exercises,
                                estimatedDurationMinutes = exercises.estimatedDurationMinutes(),
                            )
                        ).fold(
                            ifLeft = { f ->
                                isSaving = false; errorMessage = f.toWorkoutUserMessage()
                            },
                            ifRight = { isSaving = false; isDirty = false; onSaved() }
                        )
                    }
                }

                WorkoutEditEvent.Delete -> showDeleteDialog = true
                WorkoutEditEvent.DismissDelete -> showDeleteDialog = false
                WorkoutEditEvent.ConfirmDelete -> {
                    scope.launch {
                        if (mode is CreateEditMode.Edit) {
                            deleteWorkout(mode.id).fold(
                                ifLeft = { f -> errorMessage = f.toWorkoutUserMessage() },
                                ifRight = { onDeleted() }
                            )
                        }
                    }
                }

                WorkoutEditEvent.RequestBack -> if (isDirty) showDiscardDialog = true else onBack()
                WorkoutEditEvent.ConfirmDiscard -> {
                    showDiscardDialog = false; onBack()
                }

                WorkoutEditEvent.DismissDiscard -> showDiscardDialog = false

                WorkoutEditEvent.StartTimer -> {
                    if (mode is CreateEditMode.Edit) {
                        onStartTimer?.invoke(ItemType.Workout, mode.id)
                    }
                }
            }
        }
    }

    return WorkoutEditState(
        mode = mode,
        name = name,
        description = description,
        workoutDiscipline = workoutDiscipline,
        workoutType = workoutType,
        circuitRounds = circuitRounds,
        circuitRestBetweenRoundsSeconds = circuitRest,
        exercises = exercises,
        isLoading = isLoading,
        isSaving = isSaving,
        nameError = nameError,
        errorMessage = errorMessage,
        showDeleteDialog = showDeleteDialog,
        isDirty = isDirty,
        showDiscardDialog = showDiscardDialog,
        eventSink = eventSink,
    )
}

internal fun Failure.toWorkoutUserMessage(): String = when (this) {
    is WorkoutFailure.NotFound -> "Workout #$id no longer exists."
    is WorkoutFailure.InvalidData -> "\"$field\" cannot be blank."
    is WorkoutFailure.PersistenceFailed -> "Could not save — please try again."
    is WorkoutFailure.SerializationFailed -> "Exercise data is corrupted."
    else -> "An unexpected error occurred."
}
