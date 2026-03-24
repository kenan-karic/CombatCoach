package io.aethibo.combatcoach.shared.achievement.data.exception

sealed class AchievementException : Exception() {
    /** Thrown when a specific achievement key is not found in the database. */
    data class NotFound(val key: String) : AchievementException()

    /** Thrown when a write operation fails (e.g., SQLite full, IO error). */
    data class PersistenceFailed(override val cause: Throwable? = null) : AchievementException()

    /** Thrown if data mapping/serialization fails unexpectedly. */
    data class SerializationFailed(override val cause: Throwable? = null) : AchievementException()
}
