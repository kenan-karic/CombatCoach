package io.aethibo.combatcoach.features.progress.model

import androidx.compose.runtime.Stable

@Stable
data class WeekDay(
    val label: String,
    val sessionCount: Int,
    val isToday: Boolean,
)
