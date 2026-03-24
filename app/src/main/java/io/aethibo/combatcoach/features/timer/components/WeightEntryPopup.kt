package io.aethibo.combatcoach.features.timer.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.R
import io.aethibo.combatcoach.core.ui.components.ccTextFieldColors
import io.aethibo.combatcoach.core.ui.popup.CoachPopup
import io.aethibo.combatcoach.core.ui.popup.PopupTypes

@Composable
fun WeightEntryPopup(
    exerciseName: String,
    setIndex: Int,
    repsCompleted: Int?,
    previousWeightKg: Float?,
    onConfirm: (Float) -> Unit,
    onSkip: () -> Unit,
) {
    var raw by remember { mutableStateOf(formatWeight(previousWeightKg ?: 0f)) }
    val parsed = raw.toFloatOrNull()

    CoachPopup(
        popupType = PopupTypes.Info,
        onDismissRequest = onSkip,
        content = {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(exerciseName, style = MaterialTheme.typography.headlineSmall)
                Text(
                    text = stringResource(
                        R.string.weight_entry_set_format,
                        setIndex + 1,
                        repsCompleted ?: 0
                    ),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = raw,
                onValueChange = { raw = it.filter { c -> c.isDigit() || c == '.' } },
                label = { Text(stringResource(R.string.weight_entry_label)) },
                placeholder = { Text(stringResource(R.string.weight_entry_placeholder)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ccTextFieldColors(),
            )

            previousWeightKg?.let {
                Text(
                    text = stringResource(R.string.weight_entry_last_session, formatWeight(it)),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        },
        buttons = {
            OutlinedButton(
                onClick = onSkip,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(stringResource(R.string.action_skip))
            }
            Spacer(Modifier.width(12.dp))
            Button(
                onClick = { parsed?.let(onConfirm) },
                enabled = parsed != null && parsed > 0f,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(stringResource(R.string.action_save))
            }
        }
    )
}

private fun formatWeight(kg: Float): String =
    if (kg == kg.toLong().toFloat()) kg.toLong().toString() else kg.toString()
