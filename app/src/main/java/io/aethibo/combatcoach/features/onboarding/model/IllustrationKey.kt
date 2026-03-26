package io.aethibo.combatcoach.features.onboarding.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.SportsMartialArts
import androidx.compose.material.icons.filled.Timer
import androidx.compose.ui.graphics.vector.ImageVector

sealed class IllustrationKey(val icon: ImageVector) {
    data object Welcome : IllustrationKey(Icons.Filled.SportsMartialArts)
    data object Workouts : IllustrationKey(Icons.Filled.FitnessCenter)
    data object Timer : IllustrationKey(Icons.Filled.Timer)
    data object Progress : IllustrationKey(Icons.Filled.BarChart)
    data object Achievements : IllustrationKey(Icons.Filled.EmojiEvents)
}
