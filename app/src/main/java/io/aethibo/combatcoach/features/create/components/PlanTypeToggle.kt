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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.R
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing
import io.aethibo.combatcoach.shared.plan.domain.model.PlanType

@Composable
fun PlanTypeToggle(
    selected: PlanType,
    onSelect: (PlanType) -> Unit,
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
        PlanType.entries.forEach { type ->
            val isSelected = type == selected
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.surface else Color.Transparent
                    )
                    .clickable { onSelect(type) }
                    .padding(vertical = sp.small),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = type.label(),
                        style = MaterialTheme.typography.titleSmall,
                        color = if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        if (type == PlanType.PROGRAM)
                            stringResource(R.string.plan_type_program_subtitle)
                        else
                            stringResource(R.string.plan_type_routine_subtitle),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Preview(name = "Plan Type Toggle - Light", showBackground = true)
@Composable
private fun PlanTypeTogglePreview() {
    CombatCoachTheme {
        Column {
            // Program Selected
            PlanTypeToggle(
                selected = PlanType.PROGRAM,
                onSelect = {}
            )

            Spacer(Modifier.height(24.dp))

            // Routine Selected
            PlanTypeToggle(
                selected = PlanType.COLLECTION,
                onSelect = {}
            )
        }
    }
}

@Preview(
    name = "Plan Type Toggle - Dark",
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PlanTypeToggleDarkPreview() {
    CombatCoachTheme {
        PlanTypeToggle(
            selected = PlanType.PROGRAM,
            onSelect = {}
        )
    }
}
