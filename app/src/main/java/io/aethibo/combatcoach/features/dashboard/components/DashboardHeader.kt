package io.aethibo.combatcoach.features.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.SportsMartialArts
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.R
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing

@Composable
internal fun DashboardHeader(
    greeting: Int,
    today: String,
    onCreateWorkout: () -> Unit,
    onCreateCombo: () -> Unit,
    onCreatePlan: () -> Unit,
) {
    val sp = LocalSpacing.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = sp.screenPadding)
            .padding(top = sp.xl, bottom = sp.large),
    ) {
        Text(
            text = today,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        Spacer(Modifier.height(sp.xxs))
        Text(
            text = stringResource(greeting),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(Modifier.height(sp.large))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            QuickCreateButton(
                stringResource(R.string.dashboard_quick_create_workout),
                Icons.Filled.FitnessCenter,
                onCreateWorkout,
                Modifier.weight(1f)
            )
            Spacer(Modifier.width(sp.small))
            QuickCreateButton(
                stringResource(R.string.dashboard_quick_create_combo),
                Icons.Filled.SportsMartialArts,
                onCreateCombo,
                Modifier.weight(1f)
            )
            Spacer(Modifier.width(sp.small))
            QuickCreateButton(
                stringResource(R.string.dashboard_quick_create_plan),
                Icons.Filled.CalendarMonth,
                onCreatePlan,
                Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun QuickCreateButton(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sp = LocalSpacing.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = Color.Black.copy(alpha = 0.05f),
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(sp.small),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp),
            )
            Spacer(modifier = Modifier.height(sp.xs))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Preview(showBackground = true, name = "Dashboard header")
@Composable
private fun DashboardHeaderPreview() {
    CombatCoachTheme {
        DashboardHeader(
            greeting = R.string.dashboard_greeting_morning,
            today = "Wednesday, 26th",
            onCreateCombo = {},
            onCreateWorkout = {},
            onCreatePlan = {},
        )
    }
}
