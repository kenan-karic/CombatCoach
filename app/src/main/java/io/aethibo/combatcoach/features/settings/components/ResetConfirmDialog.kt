package io.aethibo.combatcoach.features.settings.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
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
fun ResetConfirmDialog(
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    CoachPopup(
        popupType = PopupTypes.Warning,
        onDismissRequest = onDismissRequest,
        content = {
            Text(
                text = stringResource(R.string.dialog_reset_title),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.dialog_reset_desc),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
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
                onClick = onConfirm,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Text(text = stringResource(R.string.action_reset))
            }
        }
    )
}