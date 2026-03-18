package io.aethibo.combatcoach.shared.combo.data.di

import io.aethibo.combatcoach.core.database.CombatCoachDatabase
import io.aethibo.combatcoach.shared.combo.data.repository.ComboRepositoryImpl
import io.aethibo.combatcoach.shared.combo.domain.repository.ComboRepository
import io.aethibo.combatcoach.shared.combo.domain.usecase.DeleteComboUseCase
import io.aethibo.combatcoach.shared.combo.domain.usecase.ObserveComboByIdUseCase
import io.aethibo.combatcoach.shared.combo.domain.usecase.ObserveCombosUseCase
import io.aethibo.combatcoach.shared.combo.domain.usecase.SaveComboUseCase
import io.aethibo.combatcoach.shared.combo.domain.usecase.deleteComboUseCase
import io.aethibo.combatcoach.shared.combo.domain.usecase.saveComboUseCase
import org.koin.dsl.module

val comboModule = module {
    single { get<CombatCoachDatabase>().comboDao() }

    single<ComboRepository> { ComboRepositoryImpl(get()) }

    // Observe use cases — plain Flow, no Either needed
    factory<ObserveCombosUseCase> {
        ObserveCombosUseCase { get<ComboRepository>().observeAll() }
    }
    factory<ObserveComboByIdUseCase> {
        ObserveComboByIdUseCase { id -> get<ComboRepository>().observeById(id) }
    }

    // Write use cases — return Either<Failure, Unit>
    factory<SaveComboUseCase> { saveComboUseCase(get()) }
    factory<DeleteComboUseCase> { deleteComboUseCase(get()) }
}
