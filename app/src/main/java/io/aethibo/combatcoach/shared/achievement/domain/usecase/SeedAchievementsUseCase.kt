package io.aethibo.combatcoach.shared.achievement.domain.usecase

import androidx.compose.runtime.Stable
import arrow.core.Either
import arrow.core.raise.catch
import arrow.core.raise.either
import io.aethibo.combatcoach.core.failure.Failure
import io.aethibo.combatcoach.core.failure.toFailure
import io.aethibo.combatcoach.shared.achievement.data.exception.AchievementException
import io.aethibo.combatcoach.shared.achievement.domain.failure.toFailure
import io.aethibo.combatcoach.shared.achievement.domain.repository.AchievementRepository
import io.aethibo.combatcoach.shared.achievement.domain.utils.defaultAchievements

@Stable
fun interface SeedAchievementsUseCase : suspend () -> Either<Failure, Unit>

suspend fun seedAchievements(
    repo: AchievementRepository
): Either<Failure, Unit> = either {
    catch({
        repo.seedDefaults(defaultAchievements())
    }) { exception ->
        val failure = when (exception) {
            is AchievementException -> exception.toFailure()
            else -> exception.toFailure()
        }
        raise(failure)
    }
}
