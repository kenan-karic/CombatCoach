package io.aethibo.combatcoach.shared.log.domain.usecase

import io.aethibo.combatcoach.shared.log.domain.model.WorkoutLog
import kotlinx.coroutines.flow.Flow

fun interface ObserveWorkoutLogsUseCase {
    operator fun invoke(): Flow<List<WorkoutLog>>
}

