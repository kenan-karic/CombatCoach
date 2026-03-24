package io.aethibo.combatcoach.features.timer.circuit

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing
import io.aethibo.combatcoach.features.timer.components.CombatControlBar
import io.aethibo.combatcoach.features.timer.components.LeaveSessionPopup
import io.aethibo.combatcoach.features.timer.components.RoundIndicator
import io.aethibo.combatcoach.features.timer.components.StopWorkoutPopup
import io.aethibo.combatcoach.features.timer.components.TimerRing
import io.aethibo.combatcoach.features.timer.components.TimerTopBar
import io.aethibo.combatcoach.features.timer.components.WorkoutCompleteSheet
import io.aethibo.combatcoach.shared.combo.domain.model.AdvanceMode
import io.aethibo.combatcoach.shared.timer.domain.model.TimerPhase

@Composable
fun CircuitTimerScreen(
    state: CircuitTimerState,
    onBack: () -> Unit,
) {
    val sp = LocalSpacing.current

    BackHandler(enabled = true) {
        state.eventSink(CircuitTimerEvent.RequestLeave)
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

    val bgTint by animateColorAsState(
        targetValue = when (state.phase) {
            TimerPhase.REST -> MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f)
            TimerPhase.COMPLETE -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            else -> Color.Transparent
        },
        animationSpec = tween(600), label = "bgTint",
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .background(bgTint),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TimerTopBar(
                title = state.workout?.name ?: "",
                elapsed = state.totalSecondsElapsed,
                onStop = { state.eventSink(CircuitTimerEvent.RequestStop) },
                progress = state.progressFraction,
            )

            Spacer(Modifier.height(sp.xl))
            RoundIndicator(currentRound = state.currentRound, totalRounds = state.totalRounds)
            Spacer(Modifier.height(sp.medium))

            if (state.totalStations > 1) {
                StationIndicator(
                    currentStation = state.currentStationIndex,
                    totalStations = state.totalStations,
                    modifier = Modifier.padding(horizontal = sp.screenPadding),
                )
                Spacer(Modifier.height(sp.small))
            }

            AnimatedContent(
                targetState = state.phase,
                transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(300)) },
                label = "phaseLabel",
            ) { ph ->
                Text(
                    text = when (ph) {
                        TimerPhase.WORK -> "WORK"; TimerPhase.REST -> "REST"; TimerPhase.COMPLETE -> "DONE"; else -> ""
                    },
                    style = MaterialTheme.typography.labelLarge,
                    color = if (ph == TimerPhase.REST) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary,
                    letterSpacing = 3.sp,
                )
            }

            Spacer(Modifier.height(sp.medium))
            TimerRing(
                secondsRemaining = state.secondsRemaining,
                totalSeconds = state.currentPhaseDuration,
                phase = state.phase,
                isPaused = state.isPaused,
                size = 260.dp,
            )
            Spacer(Modifier.height(sp.large))

            AnimatedContent(
                targetState = state.currentStationIndex,
                transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(300)) },
                label = "stationName",
            ) { currentIndex ->
                Text(
                    text = if (state.phase == TimerPhase.REST) "Rest" else state.currentStation?.name
                        ?: "",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(horizontal = sp.screenPadding),
                )
            }

            Spacer(Modifier.weight(1f))

            CombatControlBar(
                phase = state.phase,
                isPaused = state.isPaused,
                advanceMode = AdvanceMode.BOTH,
                onPause = { state.eventSink(CircuitTimerEvent.PauseResume) },
                onNext = { state.eventSink(CircuitTimerEvent.NextStation) },
                onSkipRest = { state.eventSink(CircuitTimerEvent.SkipRest) },
                onAddTime = { state.eventSink(CircuitTimerEvent.AddTime(it)) },
                onFinish = { state.eventSink(CircuitTimerEvent.RequestStop) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = sp.screenPadding)
                    .padding(bottom = sp.xl),
            )
        }
    }

    if (state.showStopDialog) {
        StopWorkoutPopup(
            onSaveAndStop = { state.eventSink(CircuitTimerEvent.ConfirmSaveStop) },
            onDiscardAndStop = { state.eventSink(CircuitTimerEvent.ConfirmDiscardStop) },
            onDismiss = { state.eventSink(CircuitTimerEvent.DismissStop) },
        )
    }

    if (state.showLeaveDialog) {
        LeaveSessionPopup(
            onKeepRunning = { state.eventSink(CircuitTimerEvent.KeepRunning) },
            onStopAndSave = { state.eventSink(CircuitTimerEvent.LeaveAndSave) },
            onDiscard = { state.eventSink(CircuitTimerEvent.LeaveAndDiscard) },
        )
    }

    if (state.showComplete) {
        WorkoutCompleteSheet(
            durationSeconds = state.totalSecondsElapsed,
            title = state.workout?.name ?: "Circuit",
            subtitle = "${state.totalRounds} rounds · ${state.totalStations} stations",
            onDismiss = { state.eventSink(CircuitTimerEvent.DismissComplete) },
        )
    }
}

@Composable
private fun StationIndicator(
    currentStation: Int,
    totalStations: Int,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        repeat(totalStations) { index ->
            val active = index == currentStation
            Box(
                modifier = Modifier
                    .padding(horizontal = 3.dp)
                    .background(
                        color = if (active) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                        shape = CircleShape,
                    )
                    .run { if (active) this.padding(6.dp) else this.padding(4.dp) }
            )
        }
    }
}

