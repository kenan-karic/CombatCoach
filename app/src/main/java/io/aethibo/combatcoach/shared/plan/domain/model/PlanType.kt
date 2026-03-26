package io.aethibo.combatcoach.shared.plan.domain.model

enum class PlanType {
    /** Ordered program: Day 1, Day 2 … each day has workouts/combos */
    PROGRAM,
    /** Unordered collection: cycle through workouts in any order */
    COLLECTION;

    fun label(): String = when (this) {
        PROGRAM    -> "Program"
        COLLECTION -> "Collection"
    }
}