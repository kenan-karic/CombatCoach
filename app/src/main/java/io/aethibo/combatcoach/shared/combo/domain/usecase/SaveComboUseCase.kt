package io.aethibo.combatcoach.shared.combo.domain.usecase

import arrow.core.Either
import arrow.core.raise.catch
import arrow.core.raise.either
import io.aethibo.combatcoach.core.failure.Failure
import io.aethibo.combatcoach.core.failure.toFailure
import io.aethibo.combatcoach.shared.combo.data.exception.ComboException
import io.aethibo.combatcoach.shared.combo.domain.failure.toFailure
import io.aethibo.combatcoach.shared.combo.domain.model.Combo
import io.aethibo.combatcoach.shared.combo.domain.repository.ComboRepository

/**
 * Persists a combo (insert when id==0, update otherwise).
 *
 * Returns [Either.Right] of [Unit] on success, or [Either.Left] of a
 * [ComboFailure] / [Failure.Unknown] on failure.
 */
fun interface SaveComboUseCase {
    suspend operator fun invoke(combo: Combo): Either<Failure, Unit>
}

fun saveComboUseCase(repository: ComboRepository): SaveComboUseCase =
    SaveComboUseCase { combo ->
        either {
            catch({
                repository.save(combo)
            }) { exception ->
                raise(
                    when (exception) {
                        is ComboException -> exception.toFailure()
                        else -> exception.toFailure()
                    }
                )
            }
        }
    }