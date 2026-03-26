package io.aethibo.combatcoach.features.achievements.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.R
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme
import io.aethibo.combatcoach.core.ui.theme.CoralPink
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing
import io.aethibo.combatcoach.core.ui.theme.Periwinkle

@Composable
internal fun AchievementsHeader(
    earned: Int,
    total: Int,
    fraction: Float,
    percent: Int,
) {
    val sp = LocalSpacing.current
    val animFraction by animateFloatAsState(
        targetValue = fraction,
        animationSpec = tween(800, easing = FastOutSlowInEasing),
        label = "headerProgress",
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = sp.screenPadding)
            .padding(top = sp.xl, bottom = sp.large),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.achievements_title),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(Modifier.height(sp.large))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(
                                    CoralPink.copy(alpha = 0.25f),
                                    Periwinkle.copy(alpha = 0.1f),
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        Icons.Filled.EmojiEvents,
                        null,
                        tint = CoralPink,
                        modifier = Modifier.size(36.dp),
                    )
                }

                Spacer(Modifier.width(sp.large))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = earned.toString(),
                            style = MaterialTheme.typography.displaySmall,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Text(
                            text = stringResource(R.string.achievements_count_divider, total),
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 4.dp),
                        )
                    }
                    Text(
                        text = stringResource(R.string.achievements_earned_label),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.height(sp.small))
                    LinearProgressIndicator(
                        progress = { animFraction },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.secondaryContainer,
                        strokeCap = StrokeCap.Round,
                    )
                    Spacer(Modifier.height(sp.xxs))
                    Text(
                        text = stringResource(R.string.achievements_percent_complete, percent),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Achievements - Mid Progress")
@Composable
private fun AchievementsHeaderPreview() {
    CombatCoachTheme {
        AchievementsHeader(
            earned = 12,
            total = 24,
            fraction = 0.5f,
            percent = 50
        )
    }
}

@Preview(showBackground = true, name = "Achievements - Just Started")
@Composable
private fun AchievementsHeaderNewPreview() {
    CombatCoachTheme {
        AchievementsHeader(
            earned = 1,
            total = 50,
            fraction = 0.02f,
            percent = 2
        )
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AchievementsHeaderDarkPreview() {
    CombatCoachTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            AchievementsHeader(
                earned = 48,
                total = 50,
                fraction = 0.96f,
                percent = 96
            )
        }
    }
}
