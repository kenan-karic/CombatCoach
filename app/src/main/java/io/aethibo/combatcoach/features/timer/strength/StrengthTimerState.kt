package io.aethibo.combatcoach.features.timer.strength

import io.aethibo.combatcoach.shared.log.domain.model.ExerciseLog
import io.aethibo.combatcoach.shared.timer.domain.model.PendingWeightEntry
import io.aethibo.combatcoach.shared.timer.domain.model.TimerPhase
import io.aethibo.combatcoach.shared.workout.domain.model.Exercise
import io.aethibo.combatcoach.shared.workout.domain.model.Workout

data class StrengthTimerState(
    val workout: Workout? = null,
    val currentExerciseIndex: Int = 0,
    val currentSetIndex: Int = 0,
    val phase: TimerPhase = TimerPhase.IDLE,
    val restSecondsRemaining: Int = 0,
    val totalSecondsElapsed: Int = 0,
    val isLoading: Boolean = true,
    val isPaused: Boolean = false,
    val showComplete: Boolean = false,
    val showStopDialog: Boolean = false,
    val showLeaveDialog: Boolean = false,
    val exerciseLogs: List<ExerciseLog> = emptyList(),
    val previousWeights: Map<Int, Float> = emptyMap(),
    val pendingWeightEntry: PendingWeightEntry? = null,
    val eventSink: (StrengthTimerEvent) -> Unit = {},
) {
    val currentExercise: Exercise?
        get() = workout?.exercises?.getOrNull(currentExerciseIndex)

    val totalExercises: Int
        get() = workout?.exercises?.size ?: 0

    val totalSets: Int
        get() = currentExercise?.sets ?: 0

    val isResting: Boolean
        get() = phase == TimerPhase.REST

    val isComplete: Boolean
        get() = phase == TimerPhase.COMPLETE

    val progressFraction: Float
        get() {
            val total = workout?.exercises?.sumOf { it.sets } ?: return 0f
            val done = exerciseLogs.sumOf { it.setsCompleted }
            return done.toFloat() / total.toFloat()
        }

    val currentExercisePreviousWeight: Float?
        get() = currentExercise?.let { previousWeights[it.id] }
}
