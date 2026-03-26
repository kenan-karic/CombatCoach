package io.aethibo.combatcoach.features.create.workout

import io.aethibo.combatcoach.shared.utils.TrainingDiscipline
import io.aethibo.combatcoach.shared.workout.domain.model.Exercise
import io.aethibo.combatcoach.shared.workout.domain.model.WorkoutType

sealed interface WorkoutEditEvent {
    data class NameChanged(val value: String) : WorkoutEditEvent
    data class DescriptionChanged(val value: String) : WorkoutEditEvent

    data class WorkoutDisciplineChanged(val value: TrainingDiscipline) : WorkoutEditEvent
    data class TypeChanged(val value: WorkoutType) : WorkoutEditEvent
    data class CircuitRoundsChanged(val rounds: Int) : WorkoutEditEvent
    data class CircuitRestChanged(val seconds: Int) : WorkoutEditEvent
    data class AddExercise(val exercise: Exercise) : WorkoutEditEvent
    data class UpdateExercise(val exercise: Exercise) : WorkoutEditEvent
    data class RemoveExercise(val id: Int) : WorkoutEditEvent

    data class ReorderExercises(val from: Int, val to: Int) : WorkoutEditEvent
    data object Save : WorkoutEditEvent
    data object Delete : WorkoutEditEvent
    data object ConfirmDelete : WorkoutEditEvent
    data object DismissDelete : WorkoutEditEvent
    data object StartTimer : WorkoutEditEvent
    data object RequestBack : WorkoutEditEvent
    data object ConfirmDiscard : WorkoutEditEvent
    data object DismissDiscard : WorkoutEditEvent
}
