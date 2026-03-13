package io.aethibo.combatcoach.core.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing
import io.aethibo.combatcoachex.core.ui.theme.CombatCoachExTheme
import io.aethibo.combatcoachex.core.ui.theme.LocalSpacing
import io.aethibo.combatcoachex.features.shared.utils.Discipline
import io.aethibo.combatcoachex.features.shared.utils.TrainingDiscipline
import io.aethibo.combatcoachex.features.shared.utils.WorkoutDiscipline

// ── Martial-arts discipline selector (Combo screens) ──────────────────────────

/**
 * Shows only martial-arts disciplines: STRIKING, GRAPPLING, MMA.
 * STRENGTH and GENERAL are intentionally excluded — they don't belong on a combo.
 */
@Composable
fun DisciplineSelector(
    selected: Discipline,
    onSelect: (Discipline) -> Unit,
    modifier: Modifier = Modifier,
) {
    val sp = LocalSpacing.current
    val martialArts = listOf(Discipline.STRIKING, Discipline.GRAPPLING, Discipline.MMA)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(sp.xs),
    ) {
        martialArts.forEach { discipline ->
            val isSelected = discipline == selected
            val color = discipline.accentColor()
            FilterChip(
                selected = isSelected,
                onClick = { onSelect(discipline) },
                label = { Text(discipline.label(), style = MaterialTheme.typography.labelSmall) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = color.copy(alpha = 0.15f),
                    selectedLabelColor = color,
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = isSelected,
                    selectedBorderColor = color,
                    borderColor = MaterialTheme.colorScheme.outline,
                ),
            )
        }
    }
}

// ── Workout discipline selector (Workout screens) ─────────────────────────────

/**
 * Shows the full [WorkoutDiscipline] set: Strength, Circuit, Cardio, etc.
 * Horizontally scrollable so it handles any number of entries without wrapping.
 */
@Composable
fun WorkoutDisciplineSelector(
    selected: TrainingDiscipline,
    onSelect: (TrainingDiscipline) -> Unit,
    modifier: Modifier = Modifier,
) {
    val sp = LocalSpacing.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(sp.xs),
    ) {
        WorkoutDiscipline.entries.forEach { discipline ->
            val isSelected = discipline == selected
            val color = discipline.accentColor()
            FilterChip(
                selected = isSelected,
                onClick = { onSelect(discipline) },
                label = { Text(discipline.label(), style = MaterialTheme.typography.labelSmall) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = color.copy(alpha = 0.15f),
                    selectedLabelColor = color,
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = isSelected,
                    selectedBorderColor = color,
                    borderColor = MaterialTheme.colorScheme.outline,
                ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DisciplineSelectorPreview() {
    CombatCoachTheme {
        DisciplineSelector(
            selected = Discipline.STRIKING,
            onSelect = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WorkoutDisciplineSelectorPreview() {
    CombatCoachTheme {
        WorkoutDisciplineSelector(
            selected = WorkoutDiscipline.STRENGTH,
            onSelect = {}
        )
    }
}
