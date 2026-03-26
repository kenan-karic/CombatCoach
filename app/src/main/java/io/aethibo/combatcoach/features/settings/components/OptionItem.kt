package io.aethibo.combatcoach.features.settings.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing

@Composable
fun OptionItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
    accentColor: Color = MaterialTheme.colorScheme.primary,
) {
    val sp = LocalSpacing.current

    Surface(
        onClick = onSelect,
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) accentColor.copy(alpha = 0.1f) else Color.Transparent,
        tonalElevation = 0.dp,
        border = if (isSelected) BorderStroke(1.dp, accentColor) else null,
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(sp.medium),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                icon, null,
                tint = if (isSelected) accentColor else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp),
            )
            Spacer(Modifier.width(sp.medium))
            Text(
                label,
                style = MaterialTheme.typography.bodyLarge,
                color = if (isSelected) accentColor else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f),
            )
            if (isSelected) {
                Icon(
                    Icons.Filled.Check,
                    "Selected",
                    tint = accentColor,
                    modifier = Modifier.size(20.dp),
                )
            }
        }
    }
}
