package io.aethibo.combatcoach.core.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme
import io.aethibo.combatcoachex.core.ui.theme.CombatCoachExTheme
import io.aethibo.combatcoachex.features.shared.utils.TrainingDiscipline
import io.aethibo.combatcoachex.features.shared.utils.WorkoutDiscipline

@Composable
fun DisciplineChip(discipline: TrainingDiscipline) {
    val color = discipline.accentColor()

    SuggestionChip(
        onClick = { /* no-op */ },
        label = {
            Text(
                text = discipline.label().uppercase(),
                style = MaterialTheme.typography.labelSmall
            )
        },
        colors = SuggestionChipDefaults.suggestionChipColors(
            containerColor = color.copy(alpha = 0.12f),
            labelColor = color
        ),
        shape = RoundedCornerShape(20.dp)
    )
}

@Preview
@Composable
private fun DisciplineChipPreview() {
    CombatCoachTheme {
        DisciplineChip(WorkoutDiscipline.STRENGTH)
    }
}
