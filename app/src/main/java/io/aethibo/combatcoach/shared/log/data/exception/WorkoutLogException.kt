package io.aethibo.combatcoach.shared.log.data.exception

sealed class WorkoutLogException(message: String? = null) : Exception(message) {
    data class NotFound(val id: Int) : WorkoutLogException("Workout log not found: id=$id")

    data class InvalidData(val field: String) :
        WorkoutLogException("Invalid log data — field '$field' failed validation")

    data class PersistenceFailed(override val cause: Throwable?) :
        WorkoutLogException("Failed to persist workout log: ${cause?.message}")

    data class SerializationFailed(override val cause: Throwable?) :
        WorkoutLogException("Workout log serialization failed: ${cause?.message}")
}
