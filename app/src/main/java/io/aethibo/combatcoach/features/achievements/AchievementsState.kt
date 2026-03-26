package io.aethibo.combatcoach.features.achievements

import androidx.compose.runtime.Immutable
import io.aethibo.combatcoach.shared.achievement.domain.model.Achievement
import io.aethibo.combatcoach.shared.achievement.domain.model.AchievementCategory

@Immutable
data class AchievementsState(
    val allAchievements: List<Achievement> = emptyList(),
    val filteredAchievements: List<Achievement> = emptyList(),
    val earnedCount: Int = 0,
    val totalCount: Int = 0,
    val activeCategory: AchievementCategory? = null,
    val selectedAchievement: Achievement? = null,
    val recentlyEarned: Achievement? = null,
    val isLoading: Boolean = true,
    val eventSink: (AchievementsEvent) -> Unit = {},
) {
    val overallFraction: Float
        get() = if (totalCount == 0) 0f else earnedCount.toFloat() / totalCount.toFloat()

    val overallPercent: Int
        get() = (overallFraction * 100).toInt()
}
