package io.aethibo.combatcoach.features.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SportsMartialArts
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.core.ui.components.CircleBadge
import io.aethibo.combatcoach.core.ui.components.DisciplineChip
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing
import io.aethibo.combatcoach.shared.combo.domain.model.Combo
import io.aethibo.combatcoach.shared.utils.Discipline

@Composable
fun ComboCard(
    combo: Combo,
    onStart: () -> Unit,
    onOpen: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sp = LocalSpacing.current

    Card(
        onClick = onOpen,
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.05f),
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(sp.cardPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CircleBadge(
                size = 48.dp,
                backgroundColor = combo.discipline.accentColor().copy(alpha = 0.12f),
            ) {
                Icon(
                    imageVector = Icons.Filled.SportsMartialArts,
                    contentDescription = null,
                    tint = combo.discipline.accentColor(),
                    modifier = Modifier.size(24.dp),
                )
            }

            Spacer(Modifier.width(sp.medium))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = combo.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(sp.xxs))
                Text(
                    text = combo.strikesSummary.ifBlank { "No strikes defined" },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(sp.xxs))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(sp.small),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    DisciplineChip(discipline = combo.discipline)
                    Text(
                        text = "${combo.rounds}R · ${combo.durationSeconds}s",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            Spacer(Modifier.width(sp.small))

            IconButton(
                onClick = onStart,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary),
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = "Start ${combo.name}",
                    tint = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.size(20.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ComboCardPreview() {
    CombatCoachTheme {
        ComboCard(
            combo = Combo(
                id = 1,
                name = "Jab-Cross-Hook",
                discipline = Discipline.STRIKING,
                rounds = 5,
                durationSeconds = 30,
            ),
            onStart = {},
            onOpen = {},
        )
    }
}
