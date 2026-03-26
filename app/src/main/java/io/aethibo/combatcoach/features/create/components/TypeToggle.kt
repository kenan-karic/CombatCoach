package io.aethibo.combatcoach.features.create.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing
import io.aethibo.combatcoach.shared.workout.domain.model.WorkoutType

@Composable
fun TypeToggle(
    selected: WorkoutType,
    onSelect: (WorkoutType) -> Unit,
    modifier: Modifier = Modifier,
) {
    val sp = LocalSpacing.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant),
        horizontalArrangement = Arrangement.Center,
    ) {
        WorkoutType.entries.forEach { type ->
            val isSelected = type == selected
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.surface
                        else Color.Transparent
                    )
                    .clickable { onSelect(type) }
                    .padding(vertical = sp.small),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = type.label(),
                    style = MaterialTheme.typography.titleSmall,
                    color = if (isSelected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Preview(name = "Type Toggle - Light", showBackground = true)
@Composable
private fun TypeTogglePreview() {
    CombatCoachTheme {
        Column {
            // State 1: Circuit Selected
            TypeToggle(
                selected = WorkoutType.CIRCUIT,
                onSelect = {}
            )

            Spacer(Modifier.height(24.dp))

            // State 2: Combo Selected
            TypeToggle(
                selected = WorkoutType.STRENGTH,
                onSelect = {}
            )
        }
    }
}

@Preview(
    name = "Type Toggle - Dark",
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun TypeToggleDarkPreview() {
    CombatCoachTheme {
        val selectedType = remember { mutableStateOf(WorkoutType.CIRCUIT) }

        TypeToggle(
            selected = selectedType.value,
            onSelect = { selectedType.value = it }
        )
    }
}
