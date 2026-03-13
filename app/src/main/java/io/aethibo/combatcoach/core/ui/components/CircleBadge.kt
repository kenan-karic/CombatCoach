package io.aethibo.combatcoach.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme

/**
 * Circular icon badge — used in achievement/stat cards.
 */
@Composable
fun CircleBadge(
    size: Dp = 48.dp,
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = Modifier
            .size(size)
            .background(backgroundColor, CircleShape),
        contentAlignment = Alignment.Center,
        content = content,
    )
}

@Preview
@Composable
private fun CircleBadgePreview() {
    CombatCoachTheme {
        CircleBadge {
            Text("15")
        }
    }
}
