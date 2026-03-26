package io.aethibo.combatcoach.shared.plan.domain.usecase

import io.aethibo.combatcoach.shared.plan.domain.model.ActivePlan
import kotlinx.coroutines.flow.Flow

fun interface ObserveActivePlanUseCase {
    operator fun invoke(): Flow<ActivePlan?>
}