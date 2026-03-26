package io.aethibo.combatcoach.features.create.workout

import io.aethibo.combatcoach.features.create.utils.CreateEditMode
import io.aethibo.combatcoach.shared.utils.TrainingDiscipline
import io.aethibo.combatcoach.shared.utils.WorkoutDiscipline
import io.aethibo.combatcoach.shared.workout.domain.model.Exercise
import io.aethibo.combatcoach.shared.workout.domain.model.WorkoutType

data class WorkoutEditState(
    val mode: CreateEditMode = CreateEditMode.Create,
    val name: String = "",
    val description: String = "",
    val workoutDiscipline: TrainingDiscipline = WorkoutDiscipline.STRENGTH,
    val workoutType: WorkoutType = WorkoutType.STRENGTH,
    val circuitRounds: Int = 3,
    val circuitRestBetweenRoundsSeconds: Int = 60,
    val exercises: List<Exercise> = emptyList(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val nameError: String? = null,
    val errorMessage: String? = null,
    val showDeleteDialog: Boolean = false,
    val isDirty: Boolean = false,
    val showDiscardDialog: Boolean = false,
    val eventSink: (WorkoutEditEvent) -> Unit = {},
)
