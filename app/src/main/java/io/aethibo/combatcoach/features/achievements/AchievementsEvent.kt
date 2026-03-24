package io.aethibo.combatcoach.features.achievements

import io.aethibo.combatcoach.shared.achievement.domain.model.Achievement
import io.aethibo.combatcoach.shared.achievement.domain.model.AchievementCategory

sealed interface AchievementsEvent {
    data class CategorySelected(val category: AchievementCategory?) : AchievementsEvent
    data class AchievementTapped(val achievement: Achievement) : AchievementsEvent
    data object DismissDetail : AchievementsEvent
    data object DismissCelebration : AchievementsEvent
}
