package io.aethibo.combatcoach.features.create.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.R
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing

@Composable
fun SaveBottomBar(
    isSaving: Boolean,
    onSave: () -> Unit,
) {
    val sp = LocalSpacing.current
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        shadowElevation = 8.dp,
    ) {
        Button(
            onClick = onSave,
            enabled = !isSaving,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = sp.screenPadding, vertical = sp.medium)
                .height(52.dp),
            shape = RoundedCornerShape(24.dp),
            elevation = ButtonDefaults.buttonElevation(0.dp),
        ) {
            if (isSaving) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp,
                )
            } else {
                Text(
                    text = stringResource(R.string.action_save),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Preview(name = "Save Bar States - Light", showBackground = true)
@Composable
private fun SaveBottomBarPreview() {
    CombatCoachTheme {
        Column {
            // Default State
            SaveBottomBar(
                isSaving = false,
                onSave = {}
            )

            Spacer(Modifier.height(16.dp))

            // Saving State
            SaveBottomBar(
                isSaving = true,
                onSave = {}
            )
        }
    }
}

@Preview(
    name = "Save Bar - Dark Mode",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun SaveBottomBarDarkPreview() {
    CombatCoachTheme {
        SaveBottomBar(
            isSaving = false,
            onSave = {}
        )
    }
}
