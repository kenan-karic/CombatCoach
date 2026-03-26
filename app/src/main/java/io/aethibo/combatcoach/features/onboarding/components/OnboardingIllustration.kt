package io.aethibo.combatcoach.features.onboarding.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme
import io.aethibo.combatcoach.features.onboarding.model.IllustrationKey

@Composable
internal fun OnboardingIllustration(
    illustrationKey: IllustrationKey,
    accentColor: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(260.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            accentColor.copy(alpha = 0.15f),
                            accentColor.copy(alpha = 0.04f),
                            Color.Transparent,
                        )
                    )
                )
        )

        Box(
            modifier = Modifier
                .size(180.dp)
                .clip(CircleShape)
                .background(accentColor.copy(alpha = 0.10f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = illustrationKey.icon,
                contentDescription = illustrationKey.toString().lowercase(),
                tint = accentColor,
                modifier = Modifier.size(72.dp),
            )
        }

        FloatingDecoration(accentColor = accentColor)
    }
}

@Composable
private fun FloatingDecoration(
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "float")
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "floatY",
    )

    Box(modifier = modifier.fillMaxSize()) {
        // Top-right: moves down (+offsetY)
        Box(
            modifier = Modifier
                .size(16.dp)
                .align(Alignment.TopEnd)
                .offset(x = (-40).dp, y = (40 + offsetY).dp)
                .clip(CircleShape)
                .background(accentColor.copy(alpha = 0.25f))
        )
        // Bottom-left: moves up (-offsetY)
        Box(
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.BottomStart)
                .offset(x = 50.dp, y = (-20 - offsetY).dp)
                .clip(CircleShape)
                .background(accentColor.copy(alpha = 0.18f))
        )
        // Centre-right: half-speed
        Box(
            modifier = Modifier
                .size(10.dp)
                .align(Alignment.CenterEnd)
                .offset(x = (-20).dp, y = (offsetY * 0.5f).dp)
                .clip(CircleShape)
                .background(accentColor.copy(alpha = 0.30f))
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OnboardingIllustrationPreview() {
    CombatCoachTheme {
        OnboardingIllustration(
            IllustrationKey.Welcome,
            MaterialTheme.colorScheme.primary
        )
    }
}
