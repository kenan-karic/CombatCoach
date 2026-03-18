package io.aethibo.combatcoach.shared.log.data.dao.entity

import kotlinx.serialization.Serializable

@Serializable
data class ExerciseLogEntity(
    val exerciseId: Int,
    val exerciseName: String,
    val setsCompleted: Int,
    val repsPerSet: List<Int>,        // one entry per set
    val weightPerSet: List<Float>,    // kg, 0f if bodyweight
    val durationSeconds: Int?,
    val notes: String,
)
