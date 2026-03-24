package io.aethibo.combatcoach.features.progress.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.R
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing
import io.aethibo.combatcoach.features.progress.ProgressFilter

@Composable
fun ProgressEmptyState(
    filter: ProgressFilter,
    modifier: Modifier = Modifier,
) {
    val sp = LocalSpacing.current

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(sp.medium),
    ) {
        Spacer(Modifier.height(sp.xl))
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                Icons.Outlined.BarChart,
                null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(40.dp),
            )
        }
        Text(
            text = when (filter) {
                ProgressFilter.ALL -> stringResource(R.string.empty_progress_all)
                ProgressFilter.WORKOUTS -> stringResource(R.string.empty_progress_workouts)
                ProgressFilter.COMBOS -> stringResource(R.string.empty_progress_combos)
                ProgressFilter.THIS_WEEK -> stringResource(R.string.empty_progress_week)
                ProgressFilter.THIS_MONTH -> stringResource(R.string.empty_progress_month)
            },
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )
        Text(
            text = stringResource(R.string.empty_progress_description),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(showBackground = true, name = "Empty State - All")
@Composable
private fun ProgressEmptyStateAllPreview() {
    CombatCoachTheme {
        ProgressEmptyState(filter = ProgressFilter.ALL)
    }
}

@Preview(showBackground = true, name = "Empty State - Combos")
@Composable
private fun ProgressEmptyStateCombosPreview() {
    CombatCoachTheme {
        ProgressEmptyState(filter = ProgressFilter.COMBOS)
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ProgressEmptyStateDarkPreview() {
    CombatCoachTheme {
        ProgressEmptyState(filter = ProgressFilter.THIS_WEEK)
    }
}
