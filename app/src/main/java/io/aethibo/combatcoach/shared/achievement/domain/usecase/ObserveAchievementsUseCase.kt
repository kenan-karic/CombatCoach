package io.aethibo.combatcoach.shared.achievement.domain.usecase

import androidx.compose.runtime.Stable
import io.aethibo.combatcoach.shared.achievement.domain.model.Achievement
import io.aethibo.combatcoach.shared.achievement.domain.repository.AchievementRepository
import kotlinx.coroutines.flow.Flow

@Stable
fun interface ObserveAchievementsUseCase : () -> Flow<List<Achievement>>

fun observeAchievements(
    repo: AchievementRepository
): Flow<List<Achievement>> = repo.observeAll()
