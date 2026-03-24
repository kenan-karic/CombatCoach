package io.aethibo.combatcoach.features.achievements

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.aethibo.combatcoach.shared.achievement.domain.model.Achievement
import io.aethibo.combatcoach.shared.achievement.domain.model.AchievementCategory
import io.aethibo.combatcoach.shared.achievement.domain.usecase.ObserveAchievementsUseCase

@Composable
fun achievementsPresenter(
    observeAchievements: ObserveAchievementsUseCase,
    onFinished: () -> Unit = {},
): AchievementsState {
    val achievements by observeAchievements().collectAsState(initial = emptyList())
    var isLoading by remember { mutableStateOf(true) }

    // ── UI-only state ──────────────────────────────────────────────────────
    var activeCategory by remember { mutableStateOf<AchievementCategory?>(null) }
    var selectedAchievement by remember { mutableStateOf<Achievement?>(null) }
    var recentlyEarned by remember { mutableStateOf<Achievement?>(null) }
    var previouslyEarnedIds by remember { mutableStateOf<Set<Int>>(emptySet()) }

    LaunchedEffect(achievements) {
        isLoading = false
        val currentEarnedIds = achievements.filter { it.isEarned }.map { it.id }.toSet()

        if (previouslyEarnedIds.isNotEmpty()) {
            val newlyEarned = achievements.filter { a ->
                a.isEarned && a.id !in previouslyEarnedIds
            }
            if (newlyEarned.isNotEmpty()) {
                recentlyEarned = newlyEarned.maxByOrNull { it.earnedAt ?: 0L }
            }
        }

        previouslyEarnedIds = currentEarnedIds
    }

    // ── Derived state ──────────────────────────────────────────────────────
    val filtered = remember(achievements, activeCategory) {
        if (activeCategory == null) achievements
        else achievements.filter { it.category == activeCategory }
    }

    val sortedAchievements = remember(filtered) {
        val earned = filtered.filter { it.isEarned }.sortedByDescending { it.earnedAt }
        val locked = filtered.filter { !it.isEarned }.sortedByDescending { it.progressFraction }
        earned + locked
    }

    val eventSink: (AchievementsEvent) -> Unit = remember {
        { event ->
            when (event) {
                is AchievementsEvent.CategorySelected -> activeCategory = event.category
                is AchievementsEvent.AchievementTapped -> selectedAchievement = event.achievement
                AchievementsEvent.DismissDetail -> selectedAchievement = null
                AchievementsEvent.DismissCelebration -> recentlyEarned = null
            }
        }
    }

    return AchievementsState(
        allAchievements = achievements,
        filteredAchievements = sortedAchievements,
        earnedCount = achievements.count { it.isEarned },
        totalCount = achievements.size,
        activeCategory = activeCategory,
        selectedAchievement = selectedAchievement,
        recentlyEarned = recentlyEarned,
        isLoading = isLoading,
        eventSink = eventSink,
    )
}
