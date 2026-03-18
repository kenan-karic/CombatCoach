package io.aethibo.combatcoach.features.plandetail.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Hotel
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
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing
import io.aethibo.combatcoach.shared.combo.domain.model.Combo
import io.aethibo.combatcoach.shared.plan.domain.model.PlanDay
import io.aethibo.combatcoach.shared.workout.domain.model.Workout

@Composable
fun DayCard(
    day: PlanDay,
    dayIndex: Int,
    currentDayIdx: Int,
    isActivePlan: Boolean,
    workouts: Map<Int, Workout>,
    combos: Map<Int, Combo>,
    onOpenSession: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sp = LocalSpacing.current
    val isDone = isActivePlan && dayIndex < currentDayIdx
    val isCurrent = isActivePlan && dayIndex == currentDayIdx

    val borderColor = when {
        isCurrent -> MaterialTheme.colorScheme.primary
        isDone -> MaterialTheme.colorScheme.tertiary
        else -> Color.Transparent
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = if (isCurrent) 2.dp else 1.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = Color.Black.copy(alpha = 0.05f),
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(0.dp),
        border = if (isCurrent || isDone) BorderStroke(1.5.dp, borderColor) else null,
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (!day.isRestDay && (isCurrent || !isActivePlan))
                            Modifier.clickable { onOpenSession() }
                        else Modifier
                    )
                    .padding(sp.medium),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                DayBadge(dayIndex = dayIndex, isDone = isDone, isCurrent = isCurrent)
                Spacer(Modifier.width(sp.medium))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        day.label, style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        when {
                            day.isRestDay -> "Rest day"
                            day.workoutIds.isEmpty() && day.comboIds.isEmpty() -> "No sessions assigned"
                            else -> buildSessionSummary(day, workouts, combos)
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                when {
                    isDone -> Icon(
                        Icons.Filled.CheckCircle, null,
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.size(22.dp)
                    )

                    day.isRestDay -> Icon(
                        Icons.Outlined.Hotel, null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )

                    isCurrent || !isActivePlan -> Icon(
                        Icons.Filled.PlayArrow, null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            AnimatedVisibility(visible = (isCurrent || !isActivePlan) && !day.isRestDay) {
                Column(
                    modifier = Modifier.padding(
                        start = sp.medium,
                        end = sp.medium,
                        bottom = sp.medium
                    ),
                    verticalArrangement = Arrangement.spacedBy(sp.xs),
                ) {
                    day.workoutIds.forEach { id ->
                        workouts[id]?.let { workout ->
                            WorkoutSessionItemRow(
                                name = workout.name,
                                discipline = workout.workoutDiscipline,
                                detail = "${workout.exerciseCount} exercises · ${workout.estimatedDurationMinutes}m",
                                isWorkout = true,
                                onClick = onOpenSession,
                            )
                        }
                    }
                    day.comboIds.forEach { id ->
                        combos[id]?.let { combo ->
                            SessionItemRow(
                                name = combo.name,
                                discipline = combo.discipline,
                                detail = "${combo.rounds} rounds · ${combo.durationSeconds}s each",
                                isWorkout = false,
                                onClick = onOpenSession,
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun buildSessionSummary(
    day: PlanDay,
    workouts: Map<Int, Workout>,
    combos: Map<Int, Combo>,
): String {
    val parts = mutableListOf<String>()
    if (day.workoutIds.isNotEmpty())
        parts += "${day.workoutIds.size} workout${if (day.workoutIds.size > 1) "s" else ""}"
    if (day.comboIds.isNotEmpty())
        parts += "${day.comboIds.size} combo${if (day.comboIds.size > 1) "s" else ""}"
    val totalMins = day.workoutIds.sumOf { workouts[it]?.estimatedDurationMinutes ?: 0 }
    if (totalMins > 0) parts += "~${totalMins}m"
    return parts.joinToString(" · ")
}
