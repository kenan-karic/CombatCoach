package io.aethibo.combatcoach.features.timer.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing
import io.aethibo.combatcoach.shared.combo.domain.model.AdvanceMode
import io.aethibo.combatcoach.shared.timer.domain.model.TimerPhase

@Composable
fun CombatControlBar(
    phase: TimerPhase,
    isPaused: Boolean,
    advanceMode: AdvanceMode,
    onPause: () -> Unit,
    onNext: () -> Unit,
    onSkipRest: () -> Unit,
    onAddTime: (Int) -> Unit,
    onFinish: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sp = LocalSpacing.current
    var showAddTime by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(sp.small),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(sp.small),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Add time
            OutlinedButton(
                onClick = { showAddTime = true },
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            ) {
                Icon(Icons.Filled.Add, null, Modifier.size(20.dp))
            }

            // Pause / Resume
            Button(
                onClick = onPause,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isPaused) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (isPaused) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                ),
                elevation = ButtonDefaults.buttonElevation(0.dp),
            ) {
                Icon(
                    if (isPaused) Icons.Filled.PlayArrow else Icons.Filled.Pause,
                    null,
                    Modifier.size(24.dp),
                )
                Spacer(Modifier.width(sp.xs))
                Text(
                    if (isPaused) "Resume" else "Pause",
                    style = MaterialTheme.typography.labelLarge,
                )
            }

            // Stop
            OutlinedButton(
                onClick = onFinish,
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            ) {
                Icon(Icons.Filled.Stop, null, Modifier.size(20.dp))
            }
        }

        // Manual next round (shown if advanceMode != AUTO and in WORK phase)
        AnimatedVisibility(
            visible = phase == TimerPhase.WORK && advanceMode != AdvanceMode.AUTO
        ) {
            Button(
                onClick = onNext,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                ),
                elevation = ButtonDefaults.buttonElevation(0.dp),
            ) {
                Icon(Icons.Filled.SkipNext, null, Modifier.size(20.dp))
            }
        }

        // Skip rest
        AnimatedVisibility(visible = phase == TimerPhase.REST) {
            TextButton(onClick = onSkipRest) {
                Icon(Icons.Filled.SkipNext, null, Modifier.size(20.dp))
                Spacer(Modifier.width(4.dp))
                Text("Skip rest")
            }
        }
    }

    if (showAddTime) {
        AddTimeSheet(
            onAdd = { onAddTime(it); showAddTime = false },
            onDismiss = { showAddTime = false },
        )
    }
}
