package io.aethibo.combatcoach.shared.achievement.data.di

import io.aethibo.combatcoach.core.database.CombatCoachDatabase
import io.aethibo.combatcoach.shared.achievement.data.repository.AchievementRepositoryImpl
import io.aethibo.combatcoach.shared.achievement.domain.repository.AchievementRepository
import io.aethibo.combatcoach.shared.achievement.domain.usecase.CheckAndAwardAchievementsUseCase
import io.aethibo.combatcoach.shared.achievement.domain.usecase.ObserveAchievementsUseCase
import io.aethibo.combatcoach.shared.achievement.domain.usecase.SeedAchievementsUseCase
import io.aethibo.combatcoach.shared.achievement.domain.usecase.checkAndAwardAchievements
import io.aethibo.combatcoach.shared.achievement.domain.usecase.observeAchievements
import io.aethibo.combatcoach.shared.achievement.domain.usecase.seedAchievements
import org.koin.dsl.module

val achievementsModule = module {
    single { get<CombatCoachDatabase>().achievementDao() }
    single<AchievementRepository> { AchievementRepositoryImpl(get()) }

    factory<ObserveAchievementsUseCase> {
        ObserveAchievementsUseCase { observeAchievements(get()) }
    }

    factory<CheckAndAwardAchievementsUseCase> {
        CheckAndAwardAchievementsUseCase {
            checkAndAwardAchievements(
                logRepo = get(),
                achievementRepo = get(),
                workoutRepo = get(),
                comboRepo = get()
            )
        }
    }

    factory<SeedAchievementsUseCase> {
        SeedAchievementsUseCase { seedAchievements(get()) }
    }
}
