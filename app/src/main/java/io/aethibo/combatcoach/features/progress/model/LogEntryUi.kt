package io.aethibo.combatcoach.features.progress.model

import androidx.compose.runtime.Stable
import io.aethibo.combatcoach.shared.log.domain.model.WorkoutLog
import io.aethibo.combatcoach.shared.utils.TrainingDiscipline
import io.aethibo.combatcoach.shared.workout.domain.model.WorkoutType

@Stable
data class LogEntryUi(
    val log: WorkoutLog,
    val workoutName: String,
    val discipline: TrainingDiscipline,
    val type: WorkoutType,
    val previousLog: WorkoutLog?,
)
