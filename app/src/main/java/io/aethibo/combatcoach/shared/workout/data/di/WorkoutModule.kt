package io.aethibo.combatcoach.shared.workout.data.di

import io.aethibo.combatcoach.core.database.CombatCoachDatabase
import io.aethibo.combatcoach.shared.workout.domain.repository.WorkoutRepository
import io.aethibo.combatcoach.shared.workout.domain.usecase.DeleteWorkoutUseCase
import io.aethibo.combatcoach.shared.workout.domain.usecase.ObserveWorkoutByIdUseCase
import io.aethibo.combatcoach.shared.workout.domain.usecase.ObserveWorkoutsUseCase
import io.aethibo.combatcoach.shared.workout.domain.usecase.SaveWorkoutUseCase
import io.aethibo.combatcoach.shared.workout.domain.usecase.deleteWorkoutUseCase
import io.aethibo.combatcoach.shared.workout.domain.usecase.saveWorkoutUseCase
import io.aethibo.combatcoachex.features.shared.workout.data.repository.WorkoutRepositoryImpl
import org.koin.dsl.module

val workoutModule = module {
    single { get<CombatCoachDatabase>().workoutDao() }

    single<WorkoutRepository> { WorkoutRepositoryImpl(get()) }

    factory<ObserveWorkoutsUseCase> {
        ObserveWorkoutsUseCase { get<WorkoutRepository>().observeAll() }
    }
    factory<ObserveWorkoutByIdUseCase> {
        ObserveWorkoutByIdUseCase { id -> get<WorkoutRepository>().observeById(id) }
    }
    factory<SaveWorkoutUseCase>   { saveWorkoutUseCase(get()) }
    factory<DeleteWorkoutUseCase> { deleteWorkoutUseCase(get()) }
}
