package io.aethibo.combatcoach.features.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import io.aethibo.combatcoach.R
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing

@Composable
fun ToggleRow(
    icon: ImageVector,
    label: String,
    checked: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    subLabel: String? = null,
) {
    val sp = LocalSpacing.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onToggle(!checked) }
            .padding(horizontal = sp.medium, vertical = sp.small),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SettingsIcon(icon = icon)
        Spacer(Modifier.width(sp.medium))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                label,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            subLabel?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.primary,
                checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
            ),
        )
    }
}

@Preview(showBackground = true, name = "Toggle - Active with Sublabel")
@Composable
private fun ToggleRowActivePreview() {
    CombatCoachTheme {
        ToggleRow(
            icon = Icons.Default.Vibration,
            label = stringResource(R.string.settings_haptic_label),
            subLabel = stringResource(R.string.settings_haptic_sub),
            checked = true,
            onToggle = {
                // no-op
            }
        )
    }
}

@Preview(showBackground = true, name = "Toggle - Simple")
@Composable
private fun ToggleRowSimplePreview() {
    CombatCoachTheme {
        ToggleRow(
            icon = Icons.Default.CloudUpload,
            label = "Cloud Sync",
            checked = false,
            onToggle = {
                // no-op
            }
        )
    }
}
