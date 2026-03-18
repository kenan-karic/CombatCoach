package io.aethibo.combatcoach.shared.plan.domain.usecase

import arrow.core.Either
import arrow.core.raise.catch
import arrow.core.raise.either
import io.aethibo.combatcoach.core.failure.Failure
import io.aethibo.combatcoach.core.failure.toFailure
import io.aethibo.combatcoach.shared.plan.data.exception.PlanException
import io.aethibo.combatcoach.shared.plan.domain.failure.toFailure
import io.aethibo.combatcoach.shared.plan.domain.repository.PlanRepository

fun interface AdvanceActivePlanDayUseCase {
    suspend operator fun invoke(): Either<Failure, Unit>
}

fun advanceActivePlanDayUseCase(repository: PlanRepository): AdvanceActivePlanDayUseCase =
    AdvanceActivePlanDayUseCase {
        either {
            catch({ repository.advanceActivePlanDay() }) { e ->
                raise(if (e is PlanException) e.toFailure() else e.toFailure())
            }
        }
    }
