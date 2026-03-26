package io.aethibo.combatcoach.shared.log.data.di

import io.aethibo.combatcoach.core.database.CombatCoachDatabase
import io.aethibo.combatcoach.shared.log.data.repository.WorkoutLogRepositoryImpl
import io.aethibo.combatcoach.shared.log.domain.repository.WorkoutLogRepository
import io.aethibo.combatcoach.shared.log.domain.usecase.DeleteWorkoutLogUseCase
import io.aethibo.combatcoach.shared.log.domain.usecase.ObserveDashboardStatsUseCase
import io.aethibo.combatcoach.shared.log.domain.usecase.ObserveDashboardStatsUseCaseImpl
import io.aethibo.combatcoach.shared.log.domain.usecase.ObserveLogsByComboUseCase
import io.aethibo.combatcoach.shared.log.domain.usecase.ObserveLogsByWorkoutUseCase
import io.aethibo.combatcoach.shared.log.domain.usecase.ObserveWorkoutLogsUseCase
import io.aethibo.combatcoach.shared.log.domain.usecase.SaveWorkoutLogUseCase
import org.koin.dsl.module

val workoutLogModule = module {
    // DAOs
    single { get<CombatCoachDatabase>().workoutLogDao() }

    single<WorkoutLogRepository> { WorkoutLogRepositoryImpl(get()) }

    factory<ObserveWorkoutLogsUseCase> { ObserveWorkoutLogsUseCase { get<WorkoutLogRepository>().observeAll() } }
    factory<ObserveLogsByWorkoutUseCase> {
        ObserveLogsByWorkoutUseCase {
            get<WorkoutLogRepository>().observeByWorkout(
                it
            )
        }
    }
    factory<ObserveLogsByComboUseCase> {
        ObserveLogsByComboUseCase {
            get<WorkoutLogRepository>().observeByCombo(
                it
            )
        }
    }
    factory<SaveWorkoutLogUseCase> { SaveWorkoutLogUseCase { get<WorkoutLogRepository>().save(it) } }
    factory<DeleteWorkoutLogUseCase> {
        DeleteWorkoutLogUseCase { id -> get<WorkoutLogRepository>().delete(id) }
    }

    factory<ObserveDashboardStatsUseCase> {
        ObserveDashboardStatsUseCaseImpl(get())
    }
}
