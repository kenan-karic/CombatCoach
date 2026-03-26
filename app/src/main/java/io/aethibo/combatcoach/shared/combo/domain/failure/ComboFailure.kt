package io.aethibo.combatcoach.shared.combo.domain.failure

import io.aethibo.combatcoach.core.failure.Failure
import io.aethibo.combatcoach.shared.combo.data.exception.ComboException

// ── Domain failures ───────────────────────────────────────────────────────────

/**
 * All business-level failures that can occur in the combo feature.
 *
 * These are *data*, not throwables.  They travel as the Left side of
 * Either<ComboFailure, T> through use cases up to the presenter.
 */
sealed class ComboFailure : Failure.FeatureFailure() {

    /** The requested combo does not exist. */
    data class NotFound(val id: Int) : ComboFailure()

    /** A field did not pass validation before the write was attempted. */
    data class InvalidData(val field: String) : ComboFailure()

    /** A write operation could not be persisted (disk / DB error). */
    data object PersistenceFailed : ComboFailure()

    /** Strike-list JSON could not be encoded or decoded. */
    data object SerializationFailed : ComboFailure()
}

// ── Mapper ────────────────────────────────────────────────────────────────────

/**
 * Maps a [ComboException] (data layer) to its corresponding [ComboFailure]
 * (domain layer).  Call this inside every use case catch block so that no
 * raw exception escapes the domain boundary.
 *
 * Usage inside a use case:
 * ```kotlin
 * } catch (e: ComboException) {
 *     raise(e.toFailure())
 * }
 * ```
 */
fun ComboException.toFailure(): ComboFailure = when (this) {
    is ComboException.NotFound -> ComboFailure.NotFound(id)
    is ComboException.InvalidData -> ComboFailure.InvalidData(field)
    is ComboException.PersistenceFailed -> ComboFailure.PersistenceFailed
    is ComboException.SerializationFailed -> ComboFailure.SerializationFailed
}
