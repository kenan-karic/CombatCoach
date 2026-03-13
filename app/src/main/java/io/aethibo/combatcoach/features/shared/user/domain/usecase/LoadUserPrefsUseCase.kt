package io.aethibo.combatcoach.features.shared.user.domain.usecase

import androidx.compose.runtime.Stable
import arrow.core.Either
import arrow.core.raise.catch
import arrow.core.raise.either
import io.aethibo.combatcoach.core.failure.Failure
import io.aethibo.combatcoach.core.failure.toFailure
import io.aethibo.combatcoach.features.shared.user.data.exceptions.UserPrefsException
import io.aethibo.combatcoach.features.shared.user.domain.failure.mapToFailure
import io.aethibo.combatcoachex.features.shared.utils.Discipline
import io.aethibo.combatcoachex.features.shared.user.domain.model.ThemeMode
import io.aethibo.combatcoach.features.shared.user.domain.model.UserPrefs
import io.aethibo.combatcoachex.features.shared.user.domain.model.WeightUnit
import io.aethibo.combatcoach.features.shared.user.domain.repository.UserPrefsRepository
import io.aethibo.combatcoach.features.shared.user.domain.utils.PrefKeys

@Stable
fun interface LoadUserPrefsUseCase : suspend () -> Either<Failure, UserPrefs>

suspend fun loadUserPrefs(
    userPrefsRepository: UserPrefsRepository
): Either<Failure, UserPrefs> = either {
    catch({
        val themeModeRaw = userPrefsRepository.getString(PrefKeys.THEME_MODE, ThemeMode.SYSTEM.name)
        val disciplineRaw =
            userPrefsRepository.getString(PrefKeys.DEFAULT_DISCIPLINE, Discipline.GENERAL.name)
        val weightUnitRaw = userPrefsRepository.getString(PrefKeys.WEIGHT_UNIT, WeightUnit.KG.name)

        UserPrefs(
            onboardingComplete = userPrefsRepository.getBoolean(PrefKeys.ONBOARDING_COMPLETE),
            themeMode = runCatching { ThemeMode.valueOf(themeModeRaw) }
                .getOrElse { throw UserPrefsException.CorruptedValueException(PrefKeys.THEME_MODE) },
            defaultDiscipline = runCatching { Discipline.valueOf(disciplineRaw) }
                .getOrElse { throw UserPrefsException.CorruptedValueException(PrefKeys.DEFAULT_DISCIPLINE) },
            soundEnabled = userPrefsRepository.getBoolean(PrefKeys.SOUND_ENABLED, true),
            vibrationEnabled = userPrefsRepository.getBoolean(PrefKeys.VIBRATION_ENABLED, true),
            weightUnit = runCatching { WeightUnit.valueOf(weightUnitRaw) }
                .getOrElse { throw UserPrefsException.CorruptedValueException(PrefKeys.WEIGHT_UNIT) },
            countdownBeeps = userPrefsRepository.getBoolean(PrefKeys.COUNTDOWN_BEEPS, true),
        )
    }) { exception ->
        val failure = when (exception) {
            is UserPrefsException -> exception.mapToFailure()
            else -> exception.toFailure()
        }
        raise(failure)
    }
}
