package io.aethibo.combatcoach.shared.log.domain.failure

import io.aethibo.combatcoach.core.failure.Failure
import io.aethibo.combatcoach.shared.log.data.exception.WorkoutLogException

sealed class WorkoutLogFailure : Failure.FeatureFailure() {
    data class NotFound(val id: Int) : WorkoutLogFailure()
    data class InvalidData(val field: String) : WorkoutLogFailure()
    data object PersistenceFailed : WorkoutLogFailure()
    data object SerializationFailed : WorkoutLogFailure()
}

fun WorkoutLogException.toFailure(): WorkoutLogFailure = when (this) {
    is WorkoutLogException.NotFound -> WorkoutLogFailure.NotFound(id)
    is WorkoutLogException.InvalidData -> WorkoutLogFailure.InvalidData(field)
    is WorkoutLogException.PersistenceFailed -> WorkoutLogFailure.PersistenceFailed
    is WorkoutLogException.SerializationFailed -> WorkoutLogFailure.SerializationFailed
}
