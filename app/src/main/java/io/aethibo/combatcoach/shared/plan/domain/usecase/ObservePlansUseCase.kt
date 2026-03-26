package io.aethibo.combatcoach.shared.plan.domain.usecase

import io.aethibo.combatcoach.shared.plan.domain.model.Plan
import kotlinx.coroutines.flow.Flow

fun interface ObservePlansUseCase {
    operator fun invoke(): Flow<List<Plan>>
}

