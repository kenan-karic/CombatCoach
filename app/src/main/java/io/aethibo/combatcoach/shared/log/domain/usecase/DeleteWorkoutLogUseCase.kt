package io.aethibo.combatcoach.shared.log.domain.usecase

fun interface DeleteWorkoutLogUseCase {
    suspend operator fun invoke(id: Int)
}
