package io.aethibo.combatcoach.shared.workout.domain.failure

import io.aethibo.combatcoach.core.failure.Failure
import io.aethibo.combatcoach.shared.workout.data.exception.WorkoutException

sealed class WorkoutFailure : Failure.FeatureFailure() {
    data class NotFound(val id: Int) : WorkoutFailure()
    data class InvalidData(val field: String) : WorkoutFailure()
    data object PersistenceFailed : WorkoutFailure()
    data object SerializationFailed : WorkoutFailure()
}

fun WorkoutException.toFailure(): WorkoutFailure = when (this) {
    is WorkoutException.NotFound -> WorkoutFailure.NotFound(id)
    is WorkoutException.InvalidData -> WorkoutFailure.InvalidData(field)
    is WorkoutException.PersistenceFailed -> WorkoutFailure.PersistenceFailed
    is WorkoutException.SerializationFailed -> WorkoutFailure.SerializationFailed
}
