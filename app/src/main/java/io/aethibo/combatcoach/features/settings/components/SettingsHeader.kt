package io.aethibo.combatcoach.features.settings.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.R
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing

@Composable
fun SettingsHeader(isSaving: Boolean, modifier: Modifier = Modifier) {
    val sp = LocalSpacing.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = sp.screenPadding)
            .padding(top = sp.xl, bottom = sp.large),
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(sp.small),
            ) {
                Text(
                    text = stringResource(R.string.settings_title),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.weight(1f),
                )
                AnimatedVisibility(
                    visible = isSaving,
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(14.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Text(
                            text = stringResource(R.string.settings_saving_indicator),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
            Spacer(Modifier.height(sp.xs))
            Text(
                text = stringResource(R.string.settings_auto_save_hint),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Preview(showBackground = true, name = "Settings Header - Idle")
@Composable
private fun SettingsHeaderIdlePreview() {
    CombatCoachTheme {
        SettingsHeader(isSaving = false)
    }
}

@Preview(showBackground = true, name = "Settings Header - Saving")
@Composable
private fun SettingsHeaderSavingPreview() {
    CombatCoachTheme {
        SettingsHeader(isSaving = true)
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SettingsHeaderDarkPreview() {
    CombatCoachTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            SettingsHeader(isSaving = true)
        }
    }
}
