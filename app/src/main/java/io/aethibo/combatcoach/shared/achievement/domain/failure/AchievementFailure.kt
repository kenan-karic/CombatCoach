package io.aethibo.combatcoach.shared.achievement.domain.failure

import io.aethibo.combatcoach.core.failure.Failure
import io.aethibo.combatcoach.shared.achievement.data.exception.AchievementException

sealed class AchievementFailure : Failure.FeatureFailure() {

    /** The UI can use this to show a "Key Not Found" error if applicable. */
    data class NotFound(val key: String) : AchievementFailure()

    /** Generic database/disk failure. */
    data object PersistenceFailed : AchievementFailure()

    /** Mapping error between Data and Domain layers. */
    data object SerializationFailed : AchievementFailure()
}

/**
 * Maps [AchievementException] to [AchievementFailure].
 * Use this inside your Use Case catch block.
 */
fun AchievementException.toFailure(): AchievementFailure = when (this) {
    is AchievementException.NotFound -> AchievementFailure.NotFound(key)
    is AchievementException.PersistenceFailed -> AchievementFailure.PersistenceFailed
    is AchievementException.SerializationFailed -> AchievementFailure.SerializationFailed
}
