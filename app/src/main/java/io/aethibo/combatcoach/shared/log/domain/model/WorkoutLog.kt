package io.aethibo.combatcoach.shared.log.domain.model

data class WorkoutLog(
    val id: Int = 0,
    val workoutId: Int? = null,
    val comboId: Int? = null,
    val planId: Int? = null,
    val planDayNumber: Int? = null,
    val durationSeconds: Int,
    val completedAt: Long = System.currentTimeMillis(),
    val exerciseLogs: List<ExerciseLog> = emptyList(),
    val notes: String = "",
    val rating: Int = 0,               // 0 = unrated, 1–5
)
