package io.aethibo.combatcoach.features.settings.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
fun AppInfoRow(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    val sp = LocalSpacing.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = sp.medium, vertical = sp.small),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SettingsIcon(icon = icon)
        Spacer(Modifier.width(sp.medium))
        Text(
            label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
        )
        Text(
            value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Preview(showBackground = true, name = "App Info - Version")
@Composable
private fun AppInfoVersionPreview() {
    CombatCoachTheme {
        AppInfoRow(
            icon = Icons.Default.Info,
            label = stringResource(R.string.settings_info_version),
            value = "1.2.4 (Beta)"
        )
    }
}

@Preview(showBackground = true, name = "App Info - Status")
@Composable
private fun AppInfoStatusPreview() {
    CombatCoachTheme {
        AppInfoRow(
            icon = Icons.Default.Storage,
            label = stringResource(R.string.settings_info_db_status),
            value = stringResource(R.string.settings_info_online)
        )
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AppInfoDarkPreview() {
    CombatCoachTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            AppInfoRow(
                icon = Icons.Default.Fingerprint,
                label = "Build ID",
                value = "A24_99X"
            )
        }
    }
}
