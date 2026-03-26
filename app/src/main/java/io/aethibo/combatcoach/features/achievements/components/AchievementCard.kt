package io.aethibo.combatcoach.features.achievements.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing
import io.aethibo.combatcoach.shared.achievement.domain.model.Achievement
import io.aethibo.combatcoach.shared.achievement.domain.model.AchievementCategory
import io.aethibo.combatcoach.shared.achievement.domain.utils.AchievementKeys
import io.aethibo.combatcoach.shared.utils.toDisplayDate

@Composable
internal fun AchievementCard(
    achievement: Achievement,
    onTap: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sp = LocalSpacing.current
    val isEarned = achievement.isEarned
    val accentColor = achievement.category.color()

    Card(
        onClick = onTap,
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = if (isEarned) 2.dp else 1.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = Color.Black.copy(alpha = 0.05f),
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isEarned)
                MaterialTheme.colorScheme.surface
            else
                MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(sp.medium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(sp.small),
        ) {
            if (isEarned) {
                EarnedBadge(achievement = achievement, color = accentColor)
            } else {
                LockedBadge(achievement = achievement)
            }

            Text(
                text = achievement.title,
                style = MaterialTheme.typography.titleSmall,
                color = if (isEarned) MaterialTheme.colorScheme.onSurface
                else MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            if (!isEarned && achievement.progressTarget > 1) {
                val animProgress by animateFloatAsState(
                    targetValue = achievement.progressFraction,
                    animationSpec = tween(600),
                    label = "achievementProgress",
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    LinearProgressIndicator(
                        progress = { animProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = accentColor,
                        trackColor = accentColor.copy(alpha = 0.2f),
                        strokeCap = StrokeCap.Round,
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        "${achievement.progressCurrent} / ${achievement.progressTarget}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            if (isEarned) {
                achievement.earnedAt?.let { at ->
                    Text(
                        at.toDisplayDate(),
                        style = MaterialTheme.typography.labelSmall,
                        color = accentColor.copy(alpha = 0.8f),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Achievement Cards - State Comparison")
@Composable
private fun AchievementCardComparisonPreview() {
    CombatCoachTheme {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 1. Locked (No Progress showing because Target is 1)
            AchievementCard(
                achievement = Achievement(
                    key = AchievementKeys.FIRST_WORKOUT,
                    title = "First Step",
                    description = "",
                    iconRes = "",
                    category = AchievementCategory.CONSISTENCY,
                    progressTarget = 1,
                    progressCurrent = 0
                ),
                onTap = {},
                modifier = Modifier.weight(1f)
            )

            // 2. In Progress (Target > 1 shows the bar)
            AchievementCard(
                achievement = Achievement(
                    key = AchievementKeys.WORKOUTS_10,
                    title = "Consistent Warrior",
                    description = "",
                    iconRes = "",
                    category = AchievementCategory.SKILLS,
                    progressTarget = 10,
                    progressCurrent = 4
                ),
                onTap = {},
                modifier = Modifier.weight(1f)
            )

            // 3. Earned
            AchievementCard(
                achievement = Achievement(
                    key = AchievementKeys.WEEK_WARRIOR,
                    title = "Week Warrior",
                    description = "",
                    iconRes = "",
                    category = AchievementCategory.MILESTONES,
                    earnedAt = System.currentTimeMillis()
                ),
                onTap = {},
                modifier = Modifier.weight(1f)
            )
        }
    }
}
