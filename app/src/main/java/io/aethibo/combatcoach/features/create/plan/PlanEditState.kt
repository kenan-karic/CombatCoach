package io.aethibo.combatcoach.features.create.plan

import io.aethibo.combatcoach.features.create.utils.CreateEditMode
import io.aethibo.combatcoach.shared.combo.domain.model.Combo
import io.aethibo.combatcoach.shared.plan.domain.model.PlanDay
import io.aethibo.combatcoach.shared.plan.domain.model.PlanType
import io.aethibo.combatcoach.shared.utils.Discipline
import io.aethibo.combatcoach.shared.workout.domain.model.Workout

data class PlanEditState(
    val mode: CreateEditMode = CreateEditMode.Create,
    val name: String = "",
    val description: String = "",
    val discipline: Discipline = Discipline.GENERAL,
    val planType: PlanType = PlanType.PROGRAM,
    val days: List<PlanDay> = emptyList(),
    val collectionWorkoutIds: List<Int> = emptyList(),
    val availableWorkouts: List<Workout> = emptyList(),
    val availableCombos: List<Combo> = emptyList(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val nameError: String? = null,
    val errorMessage: String? = null,
    val showDeleteDialog: Boolean = false,
    val expandedDayIndex: Int? = null,
    val eventSink: (PlanEditEvent) -> Unit = {},
)
