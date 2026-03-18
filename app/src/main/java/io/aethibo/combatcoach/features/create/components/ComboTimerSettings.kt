package io.aethibo.combatcoach.features.create.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.R
import io.aethibo.combatcoach.core.ui.components.NumberField
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing
import io.aethibo.combatcoach.shared.combo.domain.model.AdvanceMode

@Composable
fun ComboTimerSettings(
    durationSeconds: Int,
    rounds: Int,
    restSeconds: Int,
    advanceMode: AdvanceMode,
    onDuration: (Int) -> Unit,
    onRounds: (Int) -> Unit,
    onRest: (Int) -> Unit,
    onAdvanceMode: (AdvanceMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    val sp = LocalSpacing.current

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(sp.small),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(sp.small)) {
            NumberField(
                label = stringResource(R.string.combo_timer_label_duration),
                durationSeconds,
                onDuration,
                Modifier.weight(1f)
            )
            NumberField(
                label = stringResource(R.string.combo_timer_label_rounds),
                rounds,
                onRounds,
                Modifier.weight(1f)
            )
            NumberField(
                label = stringResource(R.string.combo_timer_label_rest),
                restSeconds,
                onRest,
                Modifier.weight(1f)
            )
        }

        Text(
            text = stringResource(R.string.combo_timer_header_advance),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Row(horizontalArrangement = Arrangement.spacedBy(sp.xs)) {
            AdvanceMode.entries.forEach { mode ->
                val selected = mode == advanceMode
                FilterChip(
                    selected = selected,
                    onClick = { onAdvanceMode(mode) },
                    label = { Text(mode.label(), style = MaterialTheme.typography.labelSmall) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.primary,
                    ),
                )
            }
        }
    }
}

@Preview(name = "Combo Timer Settings - Light", showBackground = true)
@Preview(
    name = "Combo Timer Settings - Dark",
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun ComboTimerSettingsPreview() {
    CombatCoachTheme {
        ComboTimerSettings(
            durationSeconds = 180,
            rounds = 3,
            restSeconds = 60,
            advanceMode = AdvanceMode.AUTO,
            onDuration = {},
            onRounds = {},
            onRest = {},
            onAdvanceMode = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
