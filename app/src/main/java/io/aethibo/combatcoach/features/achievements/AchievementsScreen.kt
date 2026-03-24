package io.aethibo.combatcoach.features.achievements

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing
import io.aethibo.combatcoach.features.achievements.components.AchievementsHeader
import io.aethibo.combatcoach.features.achievements.components.CategoryTabRow
import io.aethibo.combatcoach.features.achievements.components.AchievementCard
import io.aethibo.combatcoach.features.achievements.components.AchievementDetailSheet
import io.aethibo.combatcoachex.features.achievements.presentation.components.AchievementDetailSheet
import io.aethibo.combatcoachex.features.achievements.presentation.components.CelebrationOverlay
import io.aethibo.combatcoach.shared.achievement.domain.model.Achievement
import io.aethibo.combatcoach.shared.achievement.domain.model.AchievementCategory
import io.aethibo.combatcoach.shared.achievement.domain.utils.AchievementKeys
import kotlin.collections.emptyList

@Composable
fun AchievementsScreen(state: AchievementsState, modifier: Modifier = Modifier) {
    val sp = LocalSpacing.current

    if (state.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        return
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(bottom = sp.xxl),
        ) {
            item {
                AchievementsHeader(
                    earned = state.earnedCount,
                    total = state.totalCount,
                    fraction = state.overallFraction,
                    percent = state.overallPercent,
                )
            }

            item {
                CategoryTabRow(
                    active = state.activeCategory,
                    onSelect = { state.eventSink(AchievementsEvent.CategorySelected(it)) },
                    modifier = Modifier.padding(horizontal = sp.screenPadding),
                )
                Spacer(Modifier.height(sp.medium))
            }

            if (state.filteredAchievements.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(sp.screenPadding),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            "No achievements in this category yet.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            } else {
                val rows = state.filteredAchievements.chunked(2)
                items(items = rows, key = { it.first().id }) { row ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = sp.screenPadding),
                        horizontalArrangement = Arrangement.spacedBy(
                            sp.small
                        ),
                    ) {
                        row.forEach { achievement ->
                            AchievementCard(
                                achievement = achievement,
                                onTap = {
                                    state.eventSink(AchievementsEvent.AchievementTapped(achievement))
                                },
                                modifier = Modifier.weight(1f),
                            )
                        }
                        if (row.size == 1) Spacer(Modifier.weight(1f))
                    }
                    Spacer(Modifier.height(sp.small))
                }
            }
        }

        // ── Detail sheet ───────────────────────────────────────────────────
        state.selectedAchievement?.let { achievement ->
            AchievementDetailSheet(
                achievement = achievement,
                onDismiss = { state.eventSink(AchievementsEvent.DismissDetail) },
            )
        }

        // ── Celebration overlay ────────────────────────────────────────────
        AnimatedVisibility(
            visible = state.recentlyEarned != null,
            enter = fadeIn(tween(300)),
            exit = fadeOut(tween(300)),
        ) {
            state.recentlyEarned?.let { earned ->
                CelebrationOverlay(
                    achievement = earned,
                    onDismiss = { state.eventSink(AchievementsEvent.DismissCelebration) },
                )
            }
        }
    }
}

private val previewEarned = Achievement(
    id = 1,
    key = AchievementKeys.FIRST_WORKOUT,
    title = "First Blood",
    description = "Complete your first workout.",
    category = AchievementCategory.MILESTONES,
    earnedAt = System.currentTimeMillis() - 86_400_000L,
    progressCurrent = 1,
    iconRes = "ic_achievement_first"
)

private val previewLocked = Achievement(
    id = 2,
    key = AchievementKeys.WORKOUTS_10,
    title = "Dedicated",
    description = "Complete 10 workouts.",
    category = AchievementCategory.VOLUME,
    earnedAt = null,
    progressCurrent = 4,
    progressTarget = 10,
    iconRes = "ic_achievement_first"
)

@Preview(showBackground = true, name = "Loading")
@Composable
private fun AchievementsLoadingPreview() {
    CombatCoachTheme { AchievementsScreen(state = AchievementsState(isLoading = true)) }
}

@Preview(showBackground = true, name = "Empty category")
@Composable
private fun AchievementsEmptyCategoryPreview() {
    CombatCoachTheme {
        AchievementsScreen(
            state = AchievementsState(
                isLoading = false,
                filteredAchievements = emptyList(),
                activeCategory = AchievementCategory.SPECIAL,
            )
        )
    }
}

@Preview(showBackground = true, name = "Populated")
@Composable
private fun AchievementsPopulatedPreview() {
    CombatCoachTheme {
        AchievementsScreen(
            state = AchievementsState(
                isLoading = false,
                allAchievements = listOf(previewEarned, previewLocked),
                filteredAchievements = listOf(previewEarned, previewLocked),
                earnedCount = 1,
                totalCount = 2,
            )
        )
    }
}

@Preview(showBackground = true, name = "AchievementCard — earned")
@Composable
private fun AchievementCardEarnedPreview() {
    CombatCoachTheme {
        AchievementCard(achievement = previewEarned, onTap = {})
    }
}

@Preview(showBackground = true, name = "AchievementCard — locked with progress")
@Composable
private fun AchievementCardLockedPreview() {
    CombatCoachTheme {
        AchievementCard(achievement = previewLocked, onTap = {})
    }
}

@Preview(showBackground = true, name = "Celebration overlay")
@Composable
private fun CelebrationOverlayPreview() {
    CombatCoachTheme {
        CelebrationOverlay(achievement = previewEarned, onDismiss = {})
    }
}
