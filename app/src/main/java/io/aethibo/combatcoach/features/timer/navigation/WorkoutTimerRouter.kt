package io.aethibo.combatcoach.features.timer.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.aethibo.combatcoach.core.sound.TimerSoundCoordinator
import io.aethibo.combatcoach.features.timer.circuit.CircuitTimerScreen
import io.aethibo.combatcoach.features.timer.circuit.circuitTimerPresenter
import io.aethibo.combatcoach.features.timer.strength.StrengthTimerScreen
import io.aethibo.combatcoach.features.timer.strength.strengthTimerPresenter
import io.aethibo.combatcoach.shared.achievement.domain.usecase.CheckAndAwardAchievementsUseCase
import io.aethibo.combatcoach.shared.log.domain.usecase.ObserveLogsByWorkoutUseCase
import io.aethibo.combatcoach.shared.log.domain.usecase.SaveWorkoutLogUseCase
import io.aethibo.combatcoach.shared.timer.domain.repository.TimerRepository
import io.aethibo.combatcoach.shared.user.domain.usecase.LoadUserPrefsUseCase
import io.aethibo.combatcoach.shared.workout.domain.model.WorkoutType
import io.aethibo.combatcoach.shared.workout.domain.usecase.ObserveWorkoutByIdUseCase

@Composable
fun WorkoutTimerRouter(
    workoutId: Int,
    observeWorkoutById: ObserveWorkoutByIdUseCase,
    observeWorkoutLogs: ObserveLogsByWorkoutUseCase,
    saveWorkoutLog: SaveWorkoutLogUseCase,
    checkAchievements: CheckAndAwardAchievementsUseCase,
    loadPrefs: LoadUserPrefsUseCase,
    repository: TimerRepository,
    soundCoordinator: TimerSoundCoordinator,
    onBack: () -> Unit,
) {
    var workoutType by remember { mutableStateOf<WorkoutType?>(null) }

    val strengthState = strengthTimerPresenter(
        workoutId = workoutId,
        observeWorkoutById = observeWorkoutById,
        observeWorkoutLogs = observeWorkoutLogs,
        saveWorkoutLog = saveWorkoutLog,
        checkAchievements = checkAchievements,
        onWorkoutTypeResolved = { workoutType = it },
        loadPrefs = loadPrefs,
        repository = repository,
        soundCoordinator = soundCoordinator,
        onBack = onBack,
    )

    val circuitState = circuitTimerPresenter(
        workoutId = workoutId,
        observeWorkoutById = observeWorkoutById,
        saveWorkoutLog = saveWorkoutLog,
        checkAchievements = checkAchievements,
        onWorkoutTypeResolved = { workoutType = it },
        loadPrefs = loadPrefs,
        repository = repository,
        soundCoordinator = soundCoordinator,
        onBack = onBack,
    )

    when (workoutType) {
        null -> Box(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            Alignment.Center,
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }

        WorkoutType.CIRCUIT -> CircuitTimerScreen(state = circuitState, onBack = onBack)
        WorkoutType.STRENGTH -> StrengthTimerScreen(state = strengthState, onBack = onBack)
    }
}
