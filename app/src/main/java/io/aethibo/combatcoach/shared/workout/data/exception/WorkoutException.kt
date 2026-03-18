package io.aethibo.combatcoach.shared.workout.data.exception

sealed class WorkoutException(message: String? = null) : Exception(message) {
    data class NotFound(val id: Int) : WorkoutException("Workout not found: id=$id")
    data class InvalidData(val field: String) :
        WorkoutException("Invalid workout data — field '$field' failed validation")

    data class PersistenceFailed(override val cause: Throwable?) :
        WorkoutException("Failed to persist workout: ${cause?.message}")

    data class SerializationFailed(override val cause: Throwable?) :
        WorkoutException("Workout serialisation failed: ${cause?.message}")
}
