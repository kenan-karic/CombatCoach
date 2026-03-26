package io.aethibo.combatcoach.features.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.R
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing

@Composable
fun ActionRow(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    subLabel: String? = null,
    color: Color = MaterialTheme.colorScheme.onSurface,
) {
    val sp = LocalSpacing.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = sp.medium, vertical = sp.small),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SettingsIcon(icon = icon, tint = color)
        Spacer(Modifier.width(sp.medium))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                label,
                style = MaterialTheme.typography.bodyLarge,
                color = color,
            )
            subLabel?.let {
                Text(
                    it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        Icon(
            Icons.Filled.ChevronRight,
            null,
            tint = color.copy(alpha = 0.5f),
            modifier = Modifier.size(20.dp),
        )
    }
}

@Preview(showBackground = true, name = "Action - Standard Navigation")
@Composable
private fun ActionRowStandardPreview() {
    CombatCoachTheme {
        ActionRow(
            icon = Icons.Default.Lock,
            label = stringResource(R.string.settings_action_change_password),
            onClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Action - With Sublabel")
@Composable
private fun ActionRowSublabelPreview() {
    CombatCoachTheme {
        ActionRow(
            icon = Icons.Default.Download,
            label = stringResource(R.string.settings_action_export_data),
            subLabel = stringResource(R.string.settings_action_export_sub),
            onClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Action - Destructive (Red)")
@Composable
private fun ActionRowDestructivePreview() {
    CombatCoachTheme {
        ActionRow(
            icon = Icons.AutoMirrored.Filled.Logout,
            label = stringResource(R.string.settings_action_sign_out),
            color = MaterialTheme.colorScheme.error,
            onClick = {}
        )
    }
}
