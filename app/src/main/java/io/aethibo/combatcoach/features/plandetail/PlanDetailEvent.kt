package io.aethibo.combatcoach.features.plandetail

import io.aethibo.combatcoach.features.plandetail.model.SessionType

sealed interface PlanDetailEvent {
    data object SetAsActive : PlanDetailEvent
    data object DeactivatePlan : PlanDetailEvent
    data object AdvanceDay : PlanDetailEvent
    data class OpenStartSheet(val dayIndex: Int) : PlanDetailEvent
    data object DismissStartSheet : PlanDetailEvent
    data class StartSession(val session: SessionType) : PlanDetailEvent
    data class ToggleWeek(val weekNumber: Int) : PlanDetailEvent
    data object Edit : PlanDetailEvent
}
