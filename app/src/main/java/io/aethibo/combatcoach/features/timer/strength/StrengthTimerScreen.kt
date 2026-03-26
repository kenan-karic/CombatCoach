package io.aethibo.combatcoach.features.timer.strength

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing
import io.aethibo.combatcoach.features.timer.components.ExerciseProgressList
import io.aethibo.combatcoach.features.timer.components.LeaveSessionPopup
import io.aethibo.combatcoach.features.timer.components.RestCountdown
import io.aethibo.combatcoach.features.timer.components.StopWorkoutPopup
import io.aethibo.combatcoach.features.timer.components.StrengthControlBar
import io.aethibo.combatcoach.features.timer.components.TimerTopBar
import io.aethibo.combatcoach.features.timer.components.WeightEntryPopup
import io.aethibo.combatcoach.features.timer.components.WorkoutCompleteSheet

@Composable
fun StrengthTimerScreen(
    state: StrengthTimerState,
    onBack: () -> Unit,
) {
    val sp = LocalSpacing.current

    BackHandler(enabled = true) {
        state.eventSink(StrengthTimerEvent.RequestLeave)
    }

    if (state.isLoading) {
        Box(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            Alignment.Center,
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TimerTopBar(
                title = state.workout?.name ?: "",
                elapsed = state.totalSecondsElapsed,
                onStop = { state.eventSink(StrengthTimerEvent.RequestStop) },
                progress = state.progressFraction,
            )

            Spacer(Modifier.height(sp.large))

            AnimatedContent(
                targetState = state.currentExerciseIndex,
                transitionSpec = {
                    slideInVertically { it / 2 } + fadeIn() togetherWith
                            slideOutVertically { -it / 2 } + fadeOut()
                },
                label = "exerciseName",
            ) { currentIndex ->
                Text(
                    text = state.currentExercise?.name ?: "",
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = sp.screenPadding),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Spacer(Modifier.height(sp.small))

            Text(
                text = "Set ${state.currentSetIndex + 1} of ${state.totalSets}",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
            )

            state.currentExercise?.let { ex ->
                Spacer(Modifier.height(sp.xs))
                Text(
                    text = ex.displayRepsOrDuration(),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            state.currentExercisePreviousWeight?.let { prevWeight ->
                Spacer(Modifier.height(sp.xxs))
                Text(
                    text = "Last session: ${formatWeight(prevWeight)} kg",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Spacer(Modifier.weight(1f))

            AnimatedVisibility(
                visible = state.isResting,
                enter = fadeIn() + slideInVertically { it / 3 },
                exit = fadeOut() + slideOutVertically { it / 3 },
            ) {
                RestCountdown(
                    secondsRemaining = state.restSecondsRemaining,
                    isPaused = state.isPaused,
                    onSkip = { state.eventSink(StrengthTimerEvent.SkipRest) },
                    onAddTime = { state.eventSink(StrengthTimerEvent.AddRestTime(it)) },
                )
            }

            Spacer(Modifier.weight(1f))

            state.workout?.let { w ->
                ExerciseProgressList(
                    exercises = w.exercises,
                    currentIndex = state.currentExerciseIndex,
                    modifier = Modifier.padding(horizontal = sp.screenPadding),
                )
            }

            Spacer(Modifier.height(sp.large))

            StrengthControlBar(
                isResting = state.isResting,
                onSetDone = { state.eventSink(StrengthTimerEvent.SetComplete) },
                onSkip = { state.eventSink(StrengthTimerEvent.SkipExercise) },
                onFinish = { state.eventSink(StrengthTimerEvent.RequestStop) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = sp.screenPadding)
                    .padding(bottom = sp.xl),
            )
        }
    }

    state.pendingWeightEntry?.let { entry ->
        WeightEntryPopup(
            exerciseName = entry.exerciseName,
            setIndex = entry.setIndex,
            repsCompleted = entry.repsCompleted,
            previousWeightKg = entry.previousWeightKg,
            onConfirm = { state.eventSink(StrengthTimerEvent.WeightConfirmed(it)) },
            onSkip = { state.eventSink(StrengthTimerEvent.WeightSkipped) },
        )
    }

    if (state.showStopDialog) {
        StopWorkoutPopup(
            onSaveAndStop = { state.eventSink(StrengthTimerEvent.ConfirmSaveStop) },
            onDiscardAndStop = { state.eventSink(StrengthTimerEvent.ConfirmDiscardStop) },
            onDismiss = { state.eventSink(StrengthTimerEvent.DismissStop) },
        )
    }

    if (state.showLeaveDialog) {
        LeaveSessionPopup(
            onKeepRunning = { state.eventSink(StrengthTimerEvent.KeepRunning) },
            onStopAndSave = { state.eventSink(StrengthTimerEvent.LeaveAndSave) },
            onDiscard = { state.eventSink(StrengthTimerEvent.LeaveAndDiscard) },
        )
    }

    if (state.showComplete) {
        WorkoutCompleteSheet(
            durationSeconds = state.totalSecondsElapsed,
            title = state.workout?.name ?: "Workout",
            subtitle = "${state.exerciseLogs.sumOf { it.setsCompleted }} sets completed",
            onDismiss = { state.eventSink(StrengthTimerEvent.DismissComplete) },
        )
    }
}

private fun formatWeight(kg: Float): String =
    if (kg == kg.toLong().toFloat()) kg.toLong().toString() else kg.toString()
