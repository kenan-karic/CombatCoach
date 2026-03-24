package io.aethibo.combatcoach.features.achievements.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme
import io.aethibo.combatcoach.shared.achievement.domain.model.AchievementCategory

@Composable
internal fun CategoryTabRow(
    active: AchievementCategory?,
    onSelect: (AchievementCategory?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        FilterChip(
            selected = active == null,
            onClick = { onSelect(null) },
            label = { Text("All", style = MaterialTheme.typography.labelMedium) },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                selectedLabelColor = MaterialTheme.colorScheme.primary,
            ),
        )
        AchievementCategory.entries.forEach { category ->
            val selected = active == category
            val accentColor = category.color()
            FilterChip(
                selected = selected,
                onClick = { onSelect(category) },
                label = { Text(category.label(), style = MaterialTheme.typography.labelMedium) },
                leadingIcon = {
                    Icon(
                        imageVector = category.icon(),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = accentColor.copy(alpha = 0.15f),
                    selectedLabelColor = accentColor,
                    selectedLeadingIconColor = accentColor,
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = selected,
                    selectedBorderColor = accentColor,
                    borderColor = MaterialTheme.colorScheme.outline,
                ),
            )
        }
    }
}

@Preview(showBackground = true, name = "Category Tabs - Interactive")
@Composable
private fun CategoryTabRowPreview() {
    CombatCoachTheme {
        // State to track selection in the preview
        var selectedCategory by remember { mutableStateOf<AchievementCategory?>(null) }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Filter by Type", style = MaterialTheme.typography.titleSmall)

            CategoryTabRow(
                active = selectedCategory,
                onSelect = { selectedCategory = it }
            )
        }
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CategoryTabRowDarkPreview() {
    CombatCoachTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            CategoryTabRow(
                active = AchievementCategory.SPECIAL,
                onSelect = {},
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
