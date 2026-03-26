package io.aethibo.combatcoach.shared.workout.domain.usecase

import arrow.core.Either
import arrow.core.raise.catch
import arrow.core.raise.either
import io.aethibo.combatcoach.core.failure.Failure
import io.aethibo.combatcoach.core.failure.toFailure
import io.aethibo.combatcoach.shared.workout.data.exception.WorkoutException
import io.aethibo.combatcoach.shared.workout.domain.failure.toFailure
import io.aethibo.combatcoach.shared.workout.domain.model.Workout
import io.aethibo.combatcoach.shared.workout.domain.repository.WorkoutRepository

fun interface SaveWorkoutUseCase {
    suspend operator fun invoke(workout: Workout): Either<Failure, Unit>
}

fun saveWorkoutUseCase(repository: WorkoutRepository): SaveWorkoutUseCase =
    SaveWorkoutUseCase { workout ->
        either {
            catch({ repository.save(workout) }) { e ->
                raise(if (e is WorkoutException) e.toFailure() else e.toFailure())
            }
        }
    }
