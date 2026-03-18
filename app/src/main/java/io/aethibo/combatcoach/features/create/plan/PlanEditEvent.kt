package io.aethibo.combatcoach.features.create.plan

import io.aethibo.combatcoach.shared.plan.domain.model.PlanType
import io.aethibo.combatcoach.shared.utils.Discipline

sealed interface PlanEditEvent {
    data class NameChanged(val value: String) : PlanEditEvent
    data class DescriptionChanged(val value: String) : PlanEditEvent
    data class DisciplineChanged(val value: Discipline) : PlanEditEvent
    data class TypeChanged(val value: PlanType) : PlanEditEvent
    data object AddDay : PlanEditEvent
    data class RemoveDay(val index: Int) : PlanEditEvent
    data class UpdateDayLabel(val index: Int, val label: String) : PlanEditEvent
    data class AddWorkoutToDay(val dayIndex: Int, val workoutId: Int) : PlanEditEvent
    data class RemoveWorkoutFromDay(val dayIndex: Int, val workoutId: Int) : PlanEditEvent
    data class AddComboToDay(val dayIndex: Int, val comboId: Int) : PlanEditEvent
    data class RemoveComboFromDay(val dayIndex: Int, val comboId: Int) : PlanEditEvent
    data class ToggleDayRest(val dayIndex: Int) : PlanEditEvent
    data class ToggleExpandDay(val index: Int) : PlanEditEvent
    data class AddToCollection(val workoutId: Int) : PlanEditEvent
    data class RemoveFromCollection(val workoutId: Int) : PlanEditEvent
    data object Save : PlanEditEvent
    data object Delete : PlanEditEvent
    data object ConfirmDelete : PlanEditEvent
    data object DismissDelete : PlanEditEvent
}
