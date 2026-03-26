package io.aethibo.combatcoach.shared.log.domain.model

data class ExerciseLog(
    val id: Int = 0,
    val exerciseName: String,
    val setsCompleted: Int,
    val repsPerSet: List<Int> = emptyList(),
    val weightPerSet: List<Float> = emptyList(),
    val durationSeconds: Int? = null,
    val notes: String = "",
) {
    val totalReps: Int get() = repsPerSet.sum()
    val maxWeight: Float get() = weightPerSet.maxOrNull() ?: 0f
    val totalVolume: Float get() = repsPerSet.zip(weightPerSet).sumOf { (r, w) -> (r * w).toDouble() }.toFloat()
}
