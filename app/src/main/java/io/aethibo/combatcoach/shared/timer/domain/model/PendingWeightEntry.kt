package io.aethibo.combatcoach.shared.timer.domain.model

data class PendingWeightEntry(
    val exerciseId: String,
    val exerciseName: String,
    val setIndex: Int,
    val repsCompleted: Int?,
    val previousWeightKg: Float?,
)
