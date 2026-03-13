package io.aethibo.combatcoach.features.shared.user.data.di

import io.aethibo.combatcoach.core.database.CombatCoachDatabase
import io.aethibo.combatcoach.features.shared.user.data.repository.UserPrefsRepositoryImpl
import io.aethibo.combatcoach.features.shared.user.domain.repository.UserPrefsRepository
import io.aethibo.combatcoach.features.shared.user.domain.usecase.LoadUserPrefsUseCase
import io.aethibo.combatcoach.features.shared.user.domain.usecase.SaveUserPrefsUseCase
import io.aethibo.combatcoach.features.shared.user.domain.usecase.loadUserPrefs
import io.aethibo.combatcoach.features.shared.user.domain.usecase.saveUserPrefs
import org.koin.dsl.module

val userPrefsModule = module {
    // DAOs
    single { get<CombatCoachDatabase>().userPrefsDao() }

    single<UserPrefsRepository> { UserPrefsRepositoryImpl(get()) }

    // Use cases
    factory<SaveUserPrefsUseCase> {
        SaveUserPrefsUseCase { prefs ->
            saveUserPrefs(
                prefs = prefs,
                userPrefsRepository = get()
            )
        }
    }

    factory<LoadUserPrefsUseCase> {
        LoadUserPrefsUseCase {
            loadUserPrefs(
                userPrefsRepository = get()
            )
        }
    }
}
