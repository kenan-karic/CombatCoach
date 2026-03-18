package io.aethibo.combatcoach.shared.workout.domain.usecase

import io.aethibo.combatcoach.shared.workout.domain.model.Workout
import kotlinx.coroutines.flow.Flow

fun interface ObserveWorkoutByIdUseCase {
    operator fun invoke(id: Int): Flow<Workout?>
}