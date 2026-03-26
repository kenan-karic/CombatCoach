package io.aethibo.combatcoach.features.timer.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing

@Composable
fun StrengthControlBar(
    isResting: Boolean,
    onSetDone: () -> Unit,
    onSkip: () -> Unit,
    onFinish: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sp = LocalSpacing.current

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Skip exercise
        OutlinedButton(
            onClick = onSkip,
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        ) {
            Icon(Icons.Filled.SkipNext, null, Modifier.size(20.dp))
        }

        // Set done — primary large CTA
        AnimatedVisibility(visible = !isResting) {
            Button(
                onClick = onSetDone,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(24.dp),
                elevation = ButtonDefaults.buttonElevation(0.dp),
            ) {
                Icon(Icons.Filled.Check, null, Modifier.size(20.dp))
                Spacer(Modifier.width(sp.xs))
                Text("Set Done", style = MaterialTheme.typography.labelLarge)
            }
        }

        // Finish early
        OutlinedButton(
            onClick = onFinish,
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        ) {
            Icon(Icons.Filled.Stop, null, Modifier.size(20.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StrengthControlBarPreview() {
    CombatCoachTheme {
        StrengthControlBar(
            isResting = false,
            onSetDone = {},
            onSkip = {},
            onFinish = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StrengthControlBarRestingPreview() {
    CombatCoachTheme {
        StrengthControlBar(
            isResting = true,
            onSetDone = {},
            onSkip = {},
            onFinish = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}
