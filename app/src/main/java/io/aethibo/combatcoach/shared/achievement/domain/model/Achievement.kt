package io.aethibo.combatcoach.shared.achievement.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Filter1
import androidx.compose.material.icons.filled.Filter5
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.HourglassFull
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Looks
import androidx.compose.material.icons.filled.SportsMartialArts
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import io.aethibo.combatcoach.shared.achievement.domain.utils.AchievementKeys

data class Achievement(
    val id: Int = 0,
    val key: String,
    val title: String,
    val description: String,
    val iconRes: String,
    val category: AchievementCategory,
    val earnedAt: Long? = null,
    val progressCurrent: Int = 0,
    val progressTarget: Int = 1,
) {
    val isEarned: Boolean get() = earnedAt != null
    val progressFraction: Float
        get() = (progressCurrent.toFloat() / progressTarget.toFloat()).coerceIn(
            0f,
            1f
        )

    val progressPercent: Int get() = (progressFraction * 100).toInt()

    fun iconVector(): ImageVector = when (key) {
        AchievementKeys.FIRST_WORKOUT -> Icons.Filled.FitnessCenter
        AchievementKeys.WORKOUTS_10 -> Icons.Filled.Filter1
        AchievementKeys.WORKOUTS_50 -> Icons.Filled.Filter5
        AchievementKeys.WORKOUTS_100 -> Icons.Filled.Looks
        AchievementKeys.MINUTES_60 -> Icons.Filled.HourglassBottom
        AchievementKeys.MINUTES_600 -> Icons.Filled.HourglassFull
        AchievementKeys.MINUTES_6000 -> Icons.Filled.Bolt
        AchievementKeys.CREATED_WORKOUT -> Icons.Filled.Add
        AchievementKeys.CREATED_COMBO -> Icons.Filled.SportsMartialArts
        AchievementKeys.WEEK_WARRIOR -> Icons.Filled.LocalFireDepartment
        AchievementKeys.FIRST_PLAN -> Icons.Filled.CalendarMonth
        AchievementKeys.PLAN_COMPLETE -> Icons.Filled.Flag
        AchievementKeys.RATED_WORKOUT -> Icons.Filled.Star
        AchievementKeys.PERFECT_WEEK -> Icons.Filled.EmojiEvents
        else -> Icons.Filled.EmojiEvents
    }
}
