package io.aethibo.combatcoach.features.timer.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun RoundIndicator(
    currentRound: Int,
    totalRounds: Int,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(totalRounds) { index ->
            val isDone = index < currentRound - 1
            val isCurrent = index == currentRound - 1
            val size by animateDpAsState(
                targetValue = if (isCurrent) 12.dp else 8.dp,
                animationSpec = tween(200, easing = FastOutSlowInEasing),
                label = "roundDot",
            )
            val color by animateColorAsState(
                targetValue = when {
                    isDone -> MaterialTheme.colorScheme.tertiary
                    isCurrent -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.outline
                },
                animationSpec = tween(300),
                label = "roundColor",
            )
            Box(
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}
