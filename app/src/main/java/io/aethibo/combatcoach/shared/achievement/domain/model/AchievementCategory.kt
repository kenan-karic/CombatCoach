package io.aethibo.combatcoach.shared.achievement.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import io.aethibo.combatcoach.core.ui.theme.CoralPink
import io.aethibo.combatcoach.core.ui.theme.MintGreen
import io.aethibo.combatcoach.core.ui.theme.Periwinkle
import io.aethibo.combatcoach.core.ui.theme.StrengthOrange

enum class AchievementCategory {
    CONSISTENCY, VOLUME, MILESTONES, SKILLS, SPECIAL;

    fun label(): String = when (this) {
        CONSISTENCY -> "Consistency"
        VOLUME -> "Volume"
        MILESTONES -> "Milestones"
        SKILLS -> "Skills"
        SPECIAL -> "Special"
    }

    fun color(): Color = when (this) {
        CONSISTENCY -> Periwinkle
        VOLUME -> StrengthOrange
        MILESTONES -> CoralPink
        SKILLS -> MintGreen
        SPECIAL -> Color(0xFFFFD700)
    }

    fun icon(): ImageVector = when (this) {
        CONSISTENCY -> Icons.Filled.CalendarMonth
        VOLUME -> Icons.Filled.BarChart
        MILESTONES -> Icons.Filled.Flag
        SKILLS -> Icons.Filled.Build
        SPECIAL -> Icons.Filled.Star
    }
}
