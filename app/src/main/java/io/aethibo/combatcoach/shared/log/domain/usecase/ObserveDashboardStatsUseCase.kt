package io.aethibo.combatcoach.shared.log.domain.usecase

import io.aethibo.combatcoach.shared.log.domain.model.DashboardStats
import io.aethibo.combatcoach.shared.log.domain.repository.WorkoutLogRepository
import io.aethibo.combatcoach.shared.log.domain.utils.computeStreak
import io.aethibo.combatcoach.shared.utils.startOfMonthEpoch
import io.aethibo.combatcoach.shared.utils.startOfWeekEpoch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun interface ObserveDashboardStatsUseCase {
    operator fun invoke(): Flow<DashboardStats>
}

class ObserveDashboardStatsUseCaseImpl(
    private val logRepo: WorkoutLogRepository,
) : ObserveDashboardStatsUseCase {

    override fun invoke(): Flow<DashboardStats> {
        val weekStart = startOfWeekEpoch()
        val monthStart = startOfMonthEpoch()
        return logRepo.observeAll().map { logs ->
            val thisWeek = logs.count { it.completedAt >= weekStart }
            val totalMinutes = logs.sumOf { it.durationSeconds } / 60
            DashboardStats(
                totalWorkouts = logs.size,
                workoutsThisWeek = thisWeek,
                currentStreak = computeStreak(logs),
                totalMinutes = totalMinutes,
            )
        }
    }
}