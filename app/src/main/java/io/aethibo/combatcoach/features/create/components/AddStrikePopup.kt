package io.aethibo.combatcoach.features.create.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.R
import io.aethibo.combatcoach.core.ui.components.ccTextFieldColors
import io.aethibo.combatcoach.core.ui.popup.CoachPopup
import io.aethibo.combatcoach.core.ui.popup.PopupTypes
import io.aethibo.combatcoach.core.ui.theme.Charcoal
import io.aethibo.combatcoach.core.ui.theme.MediumGray

@Composable
fun AddStrikePopup(
    onAdd: (String) -> Unit,
    onDismissRequest: () -> Unit,
) {
    var text by remember { mutableStateOf("") }
    val commonStrikes = listOf(
        "Jab", "Cross", "Left Hook", "Right Hook",
        "Left Uppercut", "Right Uppercut", "Left Body Shot", "Right Body Shot",
        "Front Kick", "Roundhouse Kick", "Low Kick", "Teep"
    )

    CoachPopup(
        popupType = PopupTypes.Info,
        onDismissRequest = onDismissRequest,
        content = {
            Text(
                text = stringResource(R.string.strike_popup_title),
                style = MaterialTheme.typography.headlineSmall,
                color = Charcoal
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text(stringResource(R.string.strike_popup_label)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ccTextFieldColors(),
                )

                Text(
                    text = stringResource(R.string.strike_popup_quick_add),
                    style = MaterialTheme.typography.labelSmall,
                    color = MediumGray
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    commonStrikes.forEach { strike ->
                        SuggestionChip(
                            onClick = { text = strike },
                            label = { Text(strike, style = MaterialTheme.typography.labelSmall) },
                            shape = RoundedCornerShape(8.dp)
                        )
                    }
                }
            }
        },
        buttons = {
            OutlinedButton(
                onClick = onDismissRequest,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(text = stringResource(R.string.action_cancel))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Button(
                onClick = { if (text.isNotBlank()) onAdd(text.trim()) },
                enabled = text.isNotBlank(),
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(text = stringResource(R.string.action_add))
            }
        }
    )
}
