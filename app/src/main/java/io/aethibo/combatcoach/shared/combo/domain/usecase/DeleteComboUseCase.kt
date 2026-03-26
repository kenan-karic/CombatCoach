package io.aethibo.combatcoach.shared.combo.domain.usecase

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.catch
import io.aethibo.combatcoach.core.failure.Failure
import io.aethibo.combatcoach.core.failure.toFailure
import io.aethibo.combatcoach.shared.combo.data.exception.ComboException
import io.aethibo.combatcoach.shared.combo.domain.failure.toFailure
import io.aethibo.combatcoach.shared.combo.domain.repository.ComboRepository

/**
 * Deletes the combo with the given [id].
 */
fun interface DeleteComboUseCase {
    suspend operator fun invoke(id: Int): Either<Failure, Unit>
}

fun deleteComboUseCase(repository: ComboRepository): DeleteComboUseCase =
    DeleteComboUseCase { id ->
        either {
            catch({
                repository.delete(id)
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