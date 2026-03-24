package io.aethibo.combatcoach.features.achievements.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme
import io.aethibo.combatcoach.shared.achievement.domain.model.Achievement
import io.aethibo.combatcoach.shared.achievement.domain.model.AchievementCategory
import io.aethibo.combatcoach.shared.achievement.domain.utils.AchievementKeys

@Composable
internal fun EarnedBadge(
    achievement: Achievement,
    color: Color,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "badgeGlow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "glowAlpha",
    )

    Box(
        modifier = Modifier.size(64.dp),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(color.copy(alpha = glowAlpha * 0.3f))
        )
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(color.copy(alpha = 0.25f), color.copy(alpha = 0.10f))
                    )
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = achievement.iconVector(),
                contentDescription = achievement.title,
                tint = color,
                modifier = Modifier.size(28.dp),
            )
        }
        Box(
            modifier = Modifier
                .size(18.dp)
                .align(Alignment.BottomEnd)
                .clip(CircleShape)
                .background(color),
            contentAlignment = Alignment.Center,
        ) {
            Icon(Icons.Filled.Check, null, tint = Color.White, modifier = Modifier.size(11.dp))
        }
    }
}

@Preview(showBackground = true, name = "Earned Badge - Strength")
@Composable
private fun EarnedBadgeStrengthPreview() {
    CombatCoachTheme {
        Box(Modifier.padding(24.dp)) {
            EarnedBadge(
                achievement = Achievement(
                    key = AchievementKeys.WORKOUTS_10,
                    title = "Getting Consistent",
                    description = "Complete 10 total workouts.",
                    iconRes = "",
                    category = AchievementCategory.SKILLS,
                    earnedAt = System.currentTimeMillis()
                ),
                color = AchievementCategory.SKILLS.color()
            )
        }
    }
}

@Preview(showBackground = true, name = "Earned Badges - All Categories")
@Composable
private fun EarnedBadgesComparisonPreview() {
    CombatCoachTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Strength (Green)
            EarnedBadge(
                achievement = Achievement(
                    key = AchievementKeys.FIRST_WORKOUT,
                    title = "First Step",
                    description = "",
                    iconRes = "",
                    category = AchievementCategory.MILESTONES,
                    earnedAt = 1L
                ),
                color = AchievementCategory.SPECIAL.color()
            )

            // Combat (Red/Coral)
            EarnedBadge(
                achievement = Achievement(
                    key = AchievementKeys.CREATED_COMBO,
                    title = "Creator",
                    description = "",
                    iconRes = "",
                    category = AchievementCategory.MILESTONES,
                    earnedAt = 1L
                ),
                color = AchievementCategory.CONSISTENCY.color()
            )

            // Milestones (Gold/Amber)
            EarnedBadge(
                achievement = Achievement(
                    key = AchievementKeys.WEEK_WARRIOR,
                    title = "Warrior",
                    description = "",
                    iconRes = "",
                    category = AchievementCategory.MILESTONES,
                    earnedAt = 1L
                ),
                color = AchievementCategory.MILESTONES.color()
            )
        }
    }
}
