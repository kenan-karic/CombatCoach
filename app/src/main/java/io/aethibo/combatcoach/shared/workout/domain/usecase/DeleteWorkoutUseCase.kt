package io.aethibo.combatcoach.shared.workout.domain.usecase

import arrow.core.Either
import arrow.core.raise.catch
import arrow.core.raise.either
import io.aethibo.combatcoach.core.failure.Failure
import io.aethibo.combatcoach.core.failure.toFailure
import io.aethibo.combatcoach.shared.workout.data.exception.WorkoutException
import io.aethibo.combatcoach.shared.workout.domain.failure.toFailure
import io.aethibo.combatcoach.shared.workout.domain.repository.WorkoutRepository

fun interface DeleteWorkoutUseCase {
    suspend operator fun invoke(id: Int): Either<Failure, Unit>
}

fun deleteWorkoutUseCase(repository: WorkoutRepository): DeleteWorkoutUseCase =
    DeleteWorkoutUseCase { id ->
        either {
            catch({ repository.delete(id) }) { e ->
                raise(if (e is WorkoutException) e.toFailure() else e.toFailure())
            }
        }
    }
