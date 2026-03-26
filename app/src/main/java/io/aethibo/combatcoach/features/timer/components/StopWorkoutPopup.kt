package io.aethibo.combatcoach.features.timer.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.R
import io.aethibo.combatcoach.core.ui.popup.CoachPopup
import io.aethibo.combatcoach.core.ui.popup.PopupTypes

@Composable
fun StopWorkoutPopup(
    onSaveAndStop: () -> Unit,
    onDiscardAndStop: () -> Unit,
    onDismiss: () -> Unit,
) {
    CoachPopup(
        popupType = PopupTypes.Warning,
        onDismissRequest = onDismiss,
        content = {
            Text(
                stringResource(R.string.stop_workout_title),
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(Modifier.height(8.dp))
            Text(
                stringResource(R.string.stop_workout_desc),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        buttons = {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = onDiscardAndStop,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(stringResource(R.string.action_discard))
                }
                Button(
                    onClick = onSaveAndStop,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text(stringResource(R.string.action_save_progress))
                }
            }
        }
    )
}
