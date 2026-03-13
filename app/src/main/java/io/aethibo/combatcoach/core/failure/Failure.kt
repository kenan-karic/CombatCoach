package io.aethibo.combatcoach.core.failure

/**
 * Base sealed class for all domain-layer failures.
 *
 * Exceptions  → thrown by the data layer (repositories)
 * Failures    → returned by the domain layer (use cases) as Either<Failure, T>
 * UiError     → produced by the presenter/ViewModel for the UI
 *
 * Never let raw exceptions escape beyond a repository, and never let
 * Failure objects appear inside a repository implementation.
 */
sealed class Failure {
    /** An unclassified, unexpected throwable escaped the mapping. */
    data class Unknown(val cause: Throwable? = null) : Failure()

    // ── Feature failures extend this ──────────────────────────────────────────

    /**
     * Base class for feature-specific failures.
     * Each feature defines its own sealed subclass of [FeatureFailure].
     *
     * Example:
     * ```
     * sealed class ComboFailure : Failure.FeatureFailure() {
     *     data object NotFound : ComboFailure()
     *     data class InvalidData(val field: String) : ComboFailure()
     * }
     * ```
     */
    abstract class FeatureFailure : Failure()
}

/**
 * Converts any [Throwable] that was NOT already mapped to a typed [Failure]
 * into [Failure.Unknown].  Use this as the final catch-all inside a use case.
 */
fun Throwable.toFailure(): Failure = Failure.Unknown(cause = this)
