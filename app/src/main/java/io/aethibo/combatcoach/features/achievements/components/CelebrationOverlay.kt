package io.aethibo.combatcoach.features.achievements.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.R
import io.aethibo.combatcoach.core.ui.theme.CoralPink
import io.aethibo.combatcoach.core.ui.theme.DeepCoral
import io.aethibo.combatcoach.core.ui.theme.DeepPeriwinkle
import io.aethibo.combatcoach.core.ui.theme.GrapplingBlue
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing
import io.aethibo.combatcoach.core.ui.theme.MintGreen
import io.aethibo.combatcoach.core.ui.theme.Periwinkle
import io.aethibo.combatcoach.shared.achievement.domain.model.Achievement
import kotlinx.coroutines.delay

@Composable
internal fun CelebrationOverlay(
    achievement: Achievement,
    onDismiss: () -> Unit,
) {
    val sp = LocalSpacing.current

    LaunchedEffect(achievement.id) {
        delay(3_500)
        onDismiss()
    }

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    val scale by animateFloatAsState(
        targetValue   = if (visible) 1f else 0f,
        animationSpec = spring(
            stiffness    = Spring.StiffnessMedium,
            dampingRatio = Spring.DampingRatioMediumBouncy,
        ),
        label = "celebrationScale",
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
            ) { onDismiss() },
        contentAlignment = Alignment.Center,
    ) {
        ConfettiEffect()

        Surface(
            modifier = Modifier
                .padding(sp.screenPadding)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                },
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 0.dp,
        ) {
            Column(
                modifier = Modifier.padding(sp.xl),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(sp.medium),
            ) {
                Text(
                    text = stringResource(R.string.achievement_unlocked_title),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                EarnedBadge(
                    achievement = achievement,
                    color = achievement.category.color(),
                )

                Text(
                    achievement.title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                )

                Text(
                    achievement.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )

                TextButton(onClick = onDismiss) {
                    Text(stringResource(R.string.action_dismiss))
                }
            }
        }
    }
}

// ─── Confetti effect ───────────────────────────────────────────────────────

@Composable
private fun ConfettiEffect() {
    val particles = remember {
        List(30) {
            ConfettiParticle(
                x = (0..100).random().toFloat(),
                speed = (2..6).random().toFloat(),
                color = confettiColors.random(),
                size = (6..14).random().toFloat(),
                rotation = (0..360).random().toFloat(),
                rotSpeed = (-4..4).filter { it != 0 }.random().toFloat(),
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "confetti")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(animation = tween(2000, easing = LinearEasing)),
        label = "confettiTime",
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        particles.forEach { p ->
            val yFrac = (time * p.speed + p.x / 100f) % 1f
            val xPos = size.width * p.x / 100f
            val yPos = size.height * yFrac
            val rot = p.rotation + time * p.rotSpeed * 360f

            drawContext.canvas.save()
            drawContext.canvas.translate(xPos, yPos)
            drawContext.canvas.rotate(rot)
            drawRect(
                color = p.color,
                size = Size(p.size, p.size * 0.6f),
                topLeft = Offset(-p.size / 2, -p.size * 0.3f),
            )
            drawContext.canvas.restore()
        }
    }
}

private data class ConfettiParticle(
    val x: Float, val speed: Float, val color: Color,
    val size: Float, val rotation: Float, val rotSpeed: Float,
)

private val confettiColors = listOf(
    Periwinkle, CoralPink, MintGreen,
    DeepPeriwinkle, DeepCoral, GrapplingBlue,
    Color(0xFFFFD700), Color(0xFFFF69B4),
)
