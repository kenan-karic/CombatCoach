package io.aethibo.combatcoach.shared.log.domain.usecase

import io.aethibo.combatcoachex.features.shared.log.domain.model.WorkoutLog
import kotlinx.coroutines.flow.Flow

fun interface ObserveLogsByWorkoutUseCase {
    operator fun invoke(workoutId: String): Flow<List<WorkoutLog>>
}