package io.aethibo.combatcoach.shared.plan.domain.failure

import io.aethibo.combatcoach.core.failure.Failure
import io.aethibo.combatcoach.shared.plan.data.exception.PlanException

sealed class PlanFailure : Failure.FeatureFailure() {
    data class NotFound(val id: Int) : PlanFailure()
    data class InvalidData(val field: String) : PlanFailure()
    data object PersistenceFailed : PlanFailure()
    data object SerializationFailed : PlanFailure()

    /** Returned when advance/clear is called but nothing is active. */
    data object NoActivePlan : PlanFailure()
}

fun PlanException.toFailure(): PlanFailure = when (this) {
    is PlanException.NotFound -> PlanFailure.NotFound(id)
    is PlanException.InvalidData -> PlanFailure.InvalidData(field)
    is PlanException.PersistenceFailed -> PlanFailure.PersistenceFailed
    is PlanException.SerializationFailed -> PlanFailure.SerializationFailed
    is PlanException.NoActivePlan -> PlanFailure.NoActivePlan
}
