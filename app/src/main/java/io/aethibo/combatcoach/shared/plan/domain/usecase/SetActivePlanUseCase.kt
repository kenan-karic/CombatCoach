package io.aethibo.combatcoach.shared.plan.domain.usecase

import arrow.core.Either
import arrow.core.raise.catch
import arrow.core.raise.either
import io.aethibo.combatcoach.core.failure.Failure
import io.aethibo.combatcoach.core.failure.toFailure
import io.aethibo.combatcoach.shared.plan.data.exception.PlanException
import io.aethibo.combatcoach.shared.plan.domain.failure.toFailure
import io.aethibo.combatcoach.shared.plan.domain.model.ActivePlan
import io.aethibo.combatcoach.shared.plan.domain.repository.PlanRepository

fun interface SetActivePlanUseCase {
    suspend operator fun invoke(planId: Int): Either<Failure, Unit>
}

fun setActivePlanUseCase(repository: PlanRepository): SetActivePlanUseCase =
    SetActivePlanUseCase { planId ->
        either {
            catch({ repository.setActivePlan(ActivePlan(planId = planId)) }) { e ->
                raise(if (e is PlanException) e.toFailure() else e.toFailure())
            }
        }
    }
