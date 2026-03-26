package io.aethibo.combatcoach.features.plan

sealed interface PlansEvent {
    data class OpenPlan(val planId: Int) : PlansEvent
    data object CreatePlan : PlansEvent
}
