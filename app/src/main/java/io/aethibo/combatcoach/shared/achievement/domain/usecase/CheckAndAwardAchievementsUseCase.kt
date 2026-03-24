package io.aethibo.combatcoach.shared.achievement.domain.usecase

import androidx.compose.runtime.Stable
import arrow.core.Either
import arrow.core.raise.catch
import arrow.core.raise.either
import io.aethibo.combatcoach.core.failure.Failure
import io.aethibo.combatcoach.core.failure.toFailure
import io.aethibo.combatcoach.shared.achievement.data.exception.AchievementException
import io.aethibo.combatcoach.shared.achievement.domain.repository.AchievementRepository
import io.aethibo.combatcoach.shared.achievement.domain.utils.AchievementKeys
import io.aethibo.combatcoach.shared.combo.data.exception.ComboException
import io.aethibo.combatcoach.shared.combo.domain.repository.ComboRepository
import io.aethibo.combatcoach.shared.log.data.exception.WorkoutLogException
import io.aethibo.combatcoach.shared.log.domain.repository.WorkoutLogRepository
import io.aethibo.combatcoach.shared.utils.startOfWeekEpoch
import io.aethibo.combatcoach.shared.workout.data.exception.WorkoutException
import io.aethibo.combatcoach.shared.workout.domain.repository.WorkoutRepository

@Stable
fun interface CheckAndAwardAchievementsUseCase : suspend () -> Either<Failure, Unit>

suspend fun checkAndAwardAchievements(
    logRepo: WorkoutLogRepository,
    achievementRepo: AchievementRepository,
    workoutRepo: WorkoutRepository,
    comboRepo: ComboRepository,
): Either<Failure, Unit> = either {
    catch({
        val totalLogs = logRepo.count()
        val weekStart = startOfWeekEpoch()
        val thisWeek = logRepo.countThisWeek(weekStart)
        val totalMinutes = logRepo.totalDurationSince(0L) / 60
        val workoutCount = workoutRepo.count()
        val comboCount = comboRepo.count()

        val checks = listOf(
            Triple(AchievementKeys.FIRST_WORKOUT, totalLogs, 1),
            Triple(AchievementKeys.WORKOUTS_10, totalLogs, 10),
            Triple(AchievementKeys.WORKOUTS_50, totalLogs, 50),
            Triple(AchievementKeys.WORKOUTS_100, totalLogs, 100),
            Triple(AchievementKeys.MINUTES_60, totalMinutes, 60),
            Triple(AchievementKeys.MINUTES_600, totalMinutes, 600),
            Triple(AchievementKeys.MINUTES_6000, totalMinutes, 6000),
            Triple(AchievementKeys.CREATED_WORKOUT, workoutCount, 1),
            Triple(AchievementKeys.CREATED_COMBO, comboCount, 1),
            Triple(AchievementKeys.WEEK_WARRIOR, thisWeek, 5)
        )

        checks.forEach { (key, current, target) ->
            awardIfThreshold(achievementRepo, key, current, target)
        }
    }) { exception ->
        val failure = when (exception) {
            is AchievementException -> exception.toFailure()
            is WorkoutException -> exception.toFailure()
            is ComboException -> exception.toFailure()
            is WorkoutLogException -> exception.toFailure()
            else -> exception.toFailure()
        }
        raise(failure)
    }
}

/** * Internal helper to handle the logic for a single achievement.
 * We don't wrap this in 'either' because it's called inside the main catch block above.
 */
private suspend fun awardIfThreshold(
    repo: AchievementRepository,
    key: String,
    current: Int,
    target: Int
) {
    val achievement = repo.getByKey(key) ?: return
    if (achievement.isEarned) return

    val earnedAt = if (current >= target) System.currentTimeMillis() else null
    repo.updateProgress(key, current.coerceAtMost(target), earnedAt)
}
