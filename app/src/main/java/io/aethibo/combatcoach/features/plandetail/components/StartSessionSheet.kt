package io.aethibo.combatcoach.features.plandetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SportsMartialArts
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.R
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing
import io.aethibo.combatcoach.features.plandetail.model.SessionType
import io.aethibo.combatcoach.shared.combo.domain.model.Combo
import io.aethibo.combatcoach.shared.plan.domain.model.PlanDay
import io.aethibo.combatcoach.shared.utils.TrainingDiscipline
import io.aethibo.combatcoach.shared.workout.domain.model.Workout

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun StartSessionSheet(
    day: PlanDay,
    workouts: Map<Int, Workout>,
    combos: Map<Int, Combo>,
    onStart: (SessionType) -> Unit,
    onDismiss: () -> Unit,
) {
    val sp = LocalSpacing.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = sp.screenPadding)
                .padding(bottom = sp.xxxl),
            verticalArrangement = Arrangement.spacedBy(sp.small),
        ) {
            Text(
                text = day.label,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = stringResource(R.string.start_session_title),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(sp.xs))

            day.workoutIds.forEach { id ->
                workouts[id]?.let { workout ->
                    StartSessionItem(
                        name = workout.name,
                        discipline = workout.workoutDiscipline,
                        detail = stringResource(
                            R.string.session_workout_detail_format,
                            workout.exerciseCount,
                            workout.estimatedDurationMinutes
                        ),
                        isWorkout = true,
                        onStart = { onStart(SessionType.Workout(id)) },
                    )
                }
            }

            day.comboIds.forEach { id ->
                combos[id]?.let { combo ->
                    StartSessionItem(
                        name = combo.name,
                        discipline = combo.discipline,
                        detail = stringResource(
                            R.string.session_combo_detail_format,
                            combo.rounds,
                            combo.durationSeconds
                        ),
                        isWorkout = false,
                        onStart = { onStart(SessionType.Combo(id)) },
                    )
                }
            }

            if (day.workoutIds.isEmpty() && day.comboIds.isEmpty()) {
                Text(
                    text = stringResource(R.string.start_session_empty),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(sp.xl),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StartSessionItem(
    name: String,
    discipline: TrainingDiscipline,
    detail: String,
    isWorkout: Boolean,
    onStart: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sp = LocalSpacing.current
    val accentColor = discipline.accentColor()

    Card(
        modifier = modifier,
        onClick = onStart,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(sp.medium),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(sp.medium),
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isWorkout) Icons.Filled.FitnessCenter else Icons.Filled.SportsMartialArts,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(22.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    name,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    detail,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Button(
                onClick = onStart,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                elevation = ButtonDefaults.buttonElevation(0.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
            ) {
                Icon(Icons.Filled.PlayArrow, null, Modifier.size(18.dp))
                Spacer(Modifier.width(4.dp))
                Text(
                    text = stringResource(R.string.start_session_action),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}