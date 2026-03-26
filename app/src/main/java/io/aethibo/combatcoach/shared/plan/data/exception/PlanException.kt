package io.aethibo.combatcoach.shared.plan.data.exception

sealed class PlanException(message: String? = null) : Exception(message) {
    data class NotFound(val id: Int) : PlanException("Plan not found: id=$id")
    data class InvalidData(val field: String) :
        PlanException("Invalid plan data — field '$field' failed validation")

    data class PersistenceFailed(override val cause: Throwable?) :
        PlanException("Failed to persist plan: ${cause?.message}")

    data class SerializationFailed(override val cause: Throwable?) :
        PlanException("Plan serialisation failed: ${cause?.message}")

    /** The active-plan slot is empty when an operation requires one. */
    data object NoActivePlan : PlanException("No active plan is set") {
        private fun readResolve(): Any = NoActivePlan
    }
}
