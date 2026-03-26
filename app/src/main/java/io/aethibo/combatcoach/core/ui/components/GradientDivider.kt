package io.aethibo.combatcoach.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme

/**
 * Full-width gradient divider.
 */
@Composable
fun GradientDivider(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(
                Brush.horizontalGradient(
                    listOf(
                        MaterialTheme.colorScheme.outline.copy(alpha = 0f),
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.6f),
                        MaterialTheme.colorScheme.outline.copy(alpha = 0f),
                    )
                )
            )
    )
}

@Preview
@Composable
private fun GradientDividerPreview() {
    CombatCoachTheme {
        GradientDivider()
    }
}
