package io.aethibo.combatcoach.shared.log.domain.model

data class DashboardStats(
    val totalWorkouts: Int = 0,
    val workoutsThisWeek: Int = 0,
    val currentStreak: Int = 0,
    val totalMinutes: Int = 0,
)