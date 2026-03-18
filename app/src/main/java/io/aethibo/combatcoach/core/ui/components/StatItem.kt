package io.aethibo.combatcoach.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import io.aethibo.combatcoach.R
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme

/**
 * Horizontal stat row — "label / value" pairs used on home & progress screens.
 */
@Composable
fun StatItem(
    label: String,
    value: String,
    valueColor: Color = MaterialTheme.colorScheme.primary,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            color = valueColor,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Preview
@Composable
private fun StatItemPreview() {
    CombatCoachTheme {
        StatItem(
            stringResource(R.string.test_title),
            stringResource(R.string.test_title)
        )
    }
}
