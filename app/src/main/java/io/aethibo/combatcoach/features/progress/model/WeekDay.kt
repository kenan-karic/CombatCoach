package io.aethibo.combatcoach.features.progress.model

data class WeekDay(
    val label: String,
    val sessionCount: Int,
    val isToday: Boolean,
)
