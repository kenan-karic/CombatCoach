package io.aethibo.combatcoach.features.plandetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme

@Composable
internal fun DayBadge(
    dayIndex: Int,
    isDone: Boolean,
    isCurrent: Boolean,
) {
    val bgColor = when {
        isDone -> MaterialTheme.colorScheme.tertiaryContainer
        isCurrent -> MaterialTheme.colorScheme.primaryContainer
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
    val textColor = when {
        isDone -> MaterialTheme.colorScheme.tertiary
        isCurrent -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(bgColor),
        contentAlignment = Alignment.Center,
    ) {
        if (isDone) {
            Icon(
                Icons.Filled.Check,
                null,
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(18.dp)
            )
        } else {
            Text("${dayIndex + 1}", style = MaterialTheme.typography.labelMedium, color = textColor)
        }
    }
}

@Preview(name = "DayBadge States - Light", showBackground = true)
@Preview(
    name = "DayBadge States - Dark",
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun DayBadgePreview() {
    CombatCoachTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Done State
            DayBadge(
                dayIndex = 0,
                isDone = true,
                isCurrent = false
            )

            // Current State
            DayBadge(
                dayIndex = 1,
                isDone = false,
                isCurrent = true
            )

            // Pending State
            DayBadge(
                dayIndex = 2,
                isDone = false,
                isCurrent = false
            )
        }
    }
}
