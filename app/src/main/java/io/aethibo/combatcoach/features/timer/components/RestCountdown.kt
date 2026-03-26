package io.aethibo.combatcoach.features.timer.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing
import io.aethibo.combatcoach.shared.utils.toTimerDisplay

@Composable
fun RestCountdown(
    secondsRemaining: Int,
    isPaused: Boolean,
    onSkip: () -> Unit,
    onAddTime: (Int) -> Unit,
) {
    val sp = LocalSpacing.current
    var showAddTime by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(sp.small),
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.tertiaryContainer,
        ) {
            Column(
                modifier = Modifier.padding(horizontal = sp.xl, vertical = sp.large),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(sp.xs),
            ) {
                Text(
                    "REST",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.tertiary,
                    letterSpacing = 2.sp,
                )
                Text(
                    text = secondsRemaining.toTimerDisplay(),
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.tertiary,
                )
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(sp.small)) {
            OutlinedButton(
                onClick = { showAddTime = true },
                enabled = !isPaused,
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
//                border = BorderStroke(
//                    1.dp,
//                    if (isPaused) MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
//                    else MaterialTheme.colorScheme.outline,
//                ),
            ) {
                Icon(Icons.Filled.Add, null, Modifier.size(20.dp))
                Spacer(Modifier.width(4.dp))
                Text("Add time")
            }
            Button(
                onClick = onSkip,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                ),
                elevation = ButtonDefaults.buttonElevation(0.dp),
            ) {
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
