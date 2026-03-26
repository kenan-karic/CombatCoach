package io.aethibo.combatcoach.features.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.R
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing

@Composable
fun PickerRow(
    icon: ImageVector,
    label: String,
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sp = LocalSpacing.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(sp.medium),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SettingsIcon(icon = icon)
        Spacer(Modifier.width(sp.medium))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        Spacer(Modifier.width(sp.xs))
        Icon(
            Icons.Filled.ChevronRight,
            null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp),
        )
    }
}

@Preview(showBackground = true, name = "Picker - Standard")
@Composable
private fun PickerRowPreview() {
    CombatCoachTheme {
        PickerRow(
            icon = Icons.Default.Scale,
            label = stringResource(R.string.settings_weight_label),
            value = stringResource(R.string.settings_weight_kg),
            onClick = {
                // no-op
            }
        )
    }
}
