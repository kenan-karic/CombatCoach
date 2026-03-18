package io.aethibo.combatcoach.features.create.components

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.R
import io.aethibo.combatcoach.core.ui.popup.CoachPopup
import io.aethibo.combatcoach.core.ui.popup.PopupTypes
import io.aethibo.combatcoach.core.ui.theme.Charcoal
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme
import io.aethibo.combatcoach.core.ui.theme.MediumGray
import io.aethibo.combatcoach.core.ui.theme.SurfaceWhite

@Composable
fun DeleteConfirmPopup(
    itemName: String,
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    CoachPopup(
        popupType = PopupTypes.Error,
        onDismissRequest = onDismissRequest,
        content = {
            Text(
                text = stringResource(R.string.delete_popup_title, itemName),
                textAlign = TextAlign.Center,
                color = Charcoal,
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.delete_popup_description),
                textAlign = TextAlign.Center,
                color = MediumGray,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        buttons = {
            OutlinedButton(
                onClick = onDismissRequest,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(
                    text = stringResource(R.string.action_cancel),
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Button(
                onClick = onConfirm,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(
                    text = stringResource(R.string.action_delete),
                    style = MaterialTheme.typography.labelMedium,
                    color = SurfaceWhite
                )
            }
        }
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun DeleteConfirmPopupPreview() {
    CombatCoachTheme {
        DeleteConfirmPopup(
            itemName = "Heavy Bag Circuit",
            onConfirm = {
                // no-op
            },
            onDismissRequest = {
                // no-op
            }
        )
    }
}
