package io.aethibo.combatcoach.shared.user.domain.usecase

import androidx.compose.runtime.Stable
import arrow.core.Either
import arrow.core.raise.catch
import arrow.core.raise.either
import io.aethibo.combatcoach.core.failure.Failure
import io.aethibo.combatcoach.core.failure.toFailure
import io.aethibo.combatcoach.shared.user.data.exceptions.UserPrefsException
import io.aethibo.combatcoach.shared.user.domain.failure.mapToFailure
import io.aethibo.combatcoach.shared.user.domain.model.UserPrefs
import io.aethibo.combatcoach.shared.user.domain.repository.UserPrefsRepository
import io.aethibo.combatcoach.shared.user.domain.utils.PrefKeys

@Stable
fun interface SaveUserPrefsUseCase : suspend (UserPrefs) -> Either<Failure, Unit>

suspend fun saveUserPrefs(
    prefs: UserPrefs,
    userPrefsRepository: UserPrefsRepository
): Either<Failure, Unit> = either {
    catch({
        userPrefsRepository.setBoolean(PrefKeys.ONBOARDING_COMPLETE, prefs.onboardingComplete)
        userPrefsRepository.setString(PrefKeys.THEME_MODE, prefs.themeMode.name)
        userPrefsRepository.setString(PrefKeys.DEFAULT_DISCIPLINE, prefs.defaultDiscipline.name)
        userPrefsRepository.setBoolean(PrefKeys.SOUND_ENABLED, prefs.soundEnabled)
        userPrefsRepository.setBoolean(PrefKeys.VIBRATION_ENABLED, prefs.vibrationEnabled)
        userPrefsRepository.setString(PrefKeys.WEIGHT_UNIT, prefs.weightUnit.name)
        userPrefsRepository.setBoolean(PrefKeys.COUNTDOWN_BEEPS, prefs.countdownBeeps)
    }) { exception ->
        val failure = when (exception) {
            is UserPrefsException -> exception.mapToFailure()
            else -> exception.toFailure()
        }
        raise(failure)
    }
}
