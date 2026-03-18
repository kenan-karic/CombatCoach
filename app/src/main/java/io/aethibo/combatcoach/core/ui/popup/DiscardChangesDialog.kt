package io.aethibo.combatcoach.core.ui.popup

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

/**
 * Shown when the user tries to navigate back with unsaved edits.
 * "Discard" confirms leaving without saving; "Keep editing" dismisses.
 */
@Composable
fun DiscardChangesDialog(
    onDiscard: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                Icons.Filled.WarningAmber,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
            )
        },
        title = { Text("Discard changes?") },
        text = {
            Text(
                "You have unsaved changes. If you go back now they will be lost.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        confirmButton = {
            Button(
                onClick = onDiscard,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                ),
                elevation = ButtonDefaults.buttonElevation(0.dp),
            ) { Text("Discard") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Keep editing") }
        },
        containerColor = MaterialTheme.colorScheme.surface,
    )
}