package io.aethibo.combatcoach.features.timer.combat

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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing
import io.aethibo.combatcoach.features.timer.components.CombatControlBar
import io.aethibo.combatcoach.features.timer.components.LeaveSessionPopup
import io.aethibo.combatcoach.features.timer.components.RoundIndicator
import io.aethibo.combatcoach.features.timer.components.StopWorkoutPopup
import io.aethibo.combatcoach.features.timer.components.StrikesDisplay
import io.aethibo.combatcoach.features.timer.components.TimerRing
import io.aethibo.combatcoach.features.timer.components.TimerTopBar
import io.aethibo.combatcoach.features.timer.components.WorkoutCompleteSheet
import io.aethibo.combatcoach.shared.combo.domain.model.AdvanceMode
import io.aethibo.combatcoach.shared.timer.domain.model.TimerPhase

@Composable
fun CombatTimerScreen(
    state: CombatTimerState,
    onBack: () -> Unit,
) {
    val sp = LocalSpacing.current

    BackHandler(enabled = true) {
        state.eventSink(CombatTimerEvent.RequestLeave)
    }

    if (state.isLoading) {
        Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background), Alignment.Center) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        return
    }

    val bgTint by animateColorAsState(
        targetValue = when (state.phase) {
            TimerPhase.REST     -> MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f)
            TimerPhase.COMPLETE -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            else                -> Color.Transparent
        },
        animationSpec = tween(600), label = "bgTint",
    )

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).background(bgTint)) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

            TimerTopBar(
                title = state.combo?.name ?: "",
                elapsed = state.totalSecondsElapsed,
                onStop = { state.eventSink(CombatTimerEvent.RequestStop) },
                progress = state.progressFraction,
            )

            Spacer(Modifier.height(sp.xl))
            RoundIndicator(currentRound = state.currentRound, totalRounds = state.totalRounds)
            Spacer(Modifier.height(sp.large))

            AnimatedContent(
                targetState = state.phase,
                transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(300)) },
                label = "phaseLabel",
            ) { ph ->
                Text(
                    text = when (ph) { TimerPhase.WORK -> "WORK"; TimerPhase.REST -> "REST"; TimerPhase.COMPLETE -> "DONE"; else -> "" },
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
            Spacer(Modifier.height(sp.xl))

            state.combo?.let { combo ->
                if (combo.strikes.isNotEmpty()) {
                    StrikesDisplay(
                        strikes = combo.strikes,
                        modifier = Modifier.padding(horizontal = sp.screenPadding),
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            CombatControlBar(
                phase = state.phase,
                isPaused = state.isPaused,
                advanceMode = state.combo?.advanceMode ?: AdvanceMode.BOTH,
                onPause = { state.eventSink(CombatTimerEvent.PauseResume) },
                onNext = { state.eventSink(CombatTimerEvent.NextRound) },
                onSkipRest = { state.eventSink(CombatTimerEvent.SkipRest) },
                onAddTime = { state.eventSink(CombatTimerEvent.AddTime(it)) },
                onFinish = { state.eventSink(CombatTimerEvent.RequestStop) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = sp.screenPadding).padding(bottom = sp.xl),
            )
        }
    }

    if (state.showStopDialog) {
        StopWorkoutPopup(
            onSaveAndStop = { state.eventSink(CombatTimerEvent.ConfirmSaveStop) },
            onDiscardAndStop = { state.eventSink(CombatTimerEvent.ConfirmDiscardStop) },
            onDismiss = { state.eventSink(CombatTimerEvent.DismissStop) },
        )
    }

    if (state.showLeaveDialog) {
        LeaveSessionPopup(
            onKeepRunning = { state.eventSink(CombatTimerEvent.KeepRunning) },
            onStopAndSave = { state.eventSink(CombatTimerEvent.LeaveAndSave) },
            onDiscard = { state.eventSink(CombatTimerEvent.LeaveAndDiscard) },
        )
    }

    if (state.showComplete) {
        WorkoutCompleteSheet(
            durationSeconds = state.totalSecondsElapsed,
            title = state.combo?.name ?: "Combo",
            subtitle = "${state.totalRounds} rounds completed",
            onDismiss = { state.eventSink(CombatTimerEvent.DismissComplete) },
        )
    }
}
