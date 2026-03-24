package io.aethibo.combatcoach.features.achievements.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme
import io.aethibo.combatcoach.shared.achievement.domain.model.Achievement
import io.aethibo.combatcoach.shared.achievement.domain.model.AchievementCategory
import io.aethibo.combatcoach.shared.achievement.domain.utils.AchievementKeys

@Composable
internal fun LockedBadge(achievement: Achievement, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(64.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = achievement.iconVector(),
            contentDescription = achievement.title,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
            modifier = Modifier.size(28.dp),
        )
        Box(
            modifier = Modifier
                .size(18.dp)
                .align(Alignment.BottomEnd)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                Icons.Filled.Lock,
                null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(11.dp)
            )
        }
    }
}

@Preview(showBackground = true, name = "Achievement States")
@Composable
private fun AchievementStatesPreview() {
    CombatCoachTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // 1. Locked / Not Started
            val locked = Achievement(
                key = AchievementKeys.WORKOUTS_10,
                title = "Getting Consistent",
                description = "Complete 10 total workouts.",
                iconRes = "",
                category = AchievementCategory.MILESTONES,
                progressCurrent = 0,
                progressTarget = 10
            )

            // 2. In Progress
            val inProgress = locked.copy(progressCurrent = 6)

            // 3. Earned
            val earned = locked.copy(
                progressCurrent = 10,
                earnedAt = System.currentTimeMillis()
            )

            Text("Locked", style = MaterialTheme.typography.labelLarge)
            LockedBadge(locked)

            Text(
                "In Progress (${inProgress.progressPercent}%)",
                style = MaterialTheme.typography.labelLarge
            )
            // You could use your LockedBadge here or a custom "In Progress" version
            LockedBadge(inProgress)

            Text("Earned", style = MaterialTheme.typography.labelLarge)
            // Assuming an EarnedBadge component exists
            EarnedBadge(earned, AchievementCategory.MILESTONES.color())
        }
    }
}
