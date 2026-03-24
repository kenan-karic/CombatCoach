package io.aethibo.combatcoach.features.achievements.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.R
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing
import io.aethibo.combatcoach.shared.achievement.domain.model.Achievement
import io.aethibo.combatcoach.shared.achievement.domain.model.AchievementCategory
import io.aethibo.combatcoach.shared.achievement.domain.utils.AchievementKeys
import io.aethibo.combatcoach.shared.utils.toDisplayDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AchievementDetailSheet(
    achievement: Achievement,
    onDismiss: () -> Unit,
) {
    val sp = LocalSpacing.current
    val accentColor = achievement.category.color()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = sp.screenPadding)
                .padding(bottom = sp.xxxl),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(sp.medium),
        ) {
            if (achievement.isEarned) {
                EarnedBadge(achievement = achievement, color = accentColor)
            } else {
                LockedBadge(achievement = achievement)
            }

            Text(
                achievement.title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = accentColor.copy(alpha = 0.12f),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        achievement.category.icon(),
                        null,
                        tint = accentColor,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        achievement.category.label(),
                        style = MaterialTheme.typography.labelMedium,
                        color = accentColor
                    )
                }
            }

            Text(
                achievement.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )

            if (!achievement.isEarned && achievement.progressTarget > 1) {
                val animProgress by animateFloatAsState(
                    targetValue = achievement.progressFraction,
                    animationSpec = tween(600),
                    label = "detailProgress",
                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    LinearProgressIndicator(
                        progress = { animProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = accentColor,
                        trackColor = accentColor.copy(alpha = 0.2f),
                        strokeCap = StrokeCap.Round,
                    )
                    Text(
                        text = stringResource(
                            R.string.achievement_detail_progress_format,
                            achievement.progressCurrent,
                            achievement.progressTarget,
                            achievement.progressPercent
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            if (achievement.isEarned) {
                achievement.earnedAt?.let { at ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                Icons.Filled.EmojiEvents,
                                null,
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = stringResource(
                                    R.string.achievement_earned_on,
                                    at.toDisplayDate()
                                ),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.tertiary,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Detail Sheet - In Progress")
@Composable
private fun AchievementDetailInProgressPreview() {
    CombatCoachTheme {
        // Note: ModalBottomSheet might not render fully in standard Previews
        // without a parent Box or Scaffold.
        AchievementDetailSheet(
            achievement = Achievement(
                key = AchievementKeys.WORKOUTS_10,
                title = "Getting Consistent",
                description = "You are on your way to building a solid habit. Keep pushing to reach 10 total sessions!",
                iconRes = "",
                category = AchievementCategory.SKILLS,
                progressCurrent = 7,
                progressTarget = 10
            ),
            onDismiss = {
                // no-op
            }
        )
    }
}

@Preview(showBackground = true, name = "Detail Sheet - Earned")
@Composable
private fun AchievementDetailEarnedPreview() {
    CombatCoachTheme {
        AchievementDetailSheet(
            achievement = Achievement(
                key = AchievementKeys.WEEK_WARRIOR,
                title = "Week Warrior",
                description = "Incredible intensity! You've crushed 5 sessions in a single week. A true display of discipline.",
                iconRes = "",
                category = AchievementCategory.MILESTONES,
                earnedAt = System.currentTimeMillis(),
                progressCurrent = 5,
                progressTarget = 5
            ),
            onDismiss = {
                // no-op
            }
        )
    }
}
