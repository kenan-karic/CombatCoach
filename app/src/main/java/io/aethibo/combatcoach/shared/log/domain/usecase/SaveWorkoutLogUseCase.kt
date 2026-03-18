package io.aethibo.combatcoach.shared.log.domain.usecase

import io.aethibo.combatcoachex.features.shared.log.domain.model.WorkoutLog

fun interface SaveWorkoutLogUseCase {
    suspend operator fun invoke(log: WorkoutLog)
}