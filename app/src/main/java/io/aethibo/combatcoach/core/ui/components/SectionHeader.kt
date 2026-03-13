package io.aethibo.combatcoach.core.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme

/**
 * Section header with consistent spacing.
 */
@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = modifier,
    )
}

@Preview
@Composable
private fun SectionHeaderPreview() {
    CombatCoachTheme {
        SectionHeader(title = "Section header")
    }
}
