package io.aethibo.combatcoach.shared.plan.data.di

import io.aethibo.combatcoach.core.database.CombatCoachDatabase
import io.aethibo.combatcoach.shared.plan.data.repository.PlanRepositoryImpl
import io.aethibo.combatcoach.shared.plan.domain.repository.PlanRepository
import io.aethibo.combatcoach.shared.plan.domain.usecase.AdvanceActivePlanDayUseCase
import io.aethibo.combatcoach.shared.plan.domain.usecase.ClearActivePlanUseCase
import io.aethibo.combatcoach.shared.plan.domain.usecase.DeletePlanUseCase
import io.aethibo.combatcoach.shared.plan.domain.usecase.ObserveActivePlanUseCase
import io.aethibo.combatcoach.shared.plan.domain.usecase.ObservePlanByIdUseCase
import io.aethibo.combatcoach.shared.plan.domain.usecase.ObservePlansUseCase
import io.aethibo.combatcoach.shared.plan.domain.usecase.SavePlanUseCase
import io.aethibo.combatcoach.shared.plan.domain.usecase.SetActivePlanUseCase
import io.aethibo.combatcoach.shared.plan.domain.usecase.advanceActivePlanDayUseCase
import io.aethibo.combatcoach.shared.plan.domain.usecase.clearActivePlanUseCase
import io.aethibo.combatcoach.shared.plan.domain.usecase.deletePlanUseCase
import io.aethibo.combatcoach.shared.plan.domain.usecase.savePlanUseCase
import io.aethibo.combatcoach.shared.plan.domain.usecase.setActivePlanUseCase
import org.koin.dsl.module

val planModule = module {
    single { get<CombatCoachDatabase>().planDao() }
    single { get<CombatCoachDatabase>().activePlanDao() }

    single<PlanRepository> { PlanRepositoryImpl(get(), get()) }

    // Observe — plain Flow
    factory<ObservePlansUseCase> {
        ObservePlansUseCase { get<PlanRepository>().observeAll() }
    }
    factory<ObservePlanByIdUseCase> {
        ObservePlanByIdUseCase { id -> get<PlanRepository>().observeById(id) }
    }
    factory<ObserveActivePlanUseCase> {
        ObserveActivePlanUseCase { get<PlanRepository>().observeActivePlan() }
    }

    // Write — Either
    factory<SavePlanUseCase> { savePlanUseCase(get()) }
    factory<DeletePlanUseCase> { deletePlanUseCase(get()) }
    factory<SetActivePlanUseCase> { setActivePlanUseCase(get()) }
    factory<ClearActivePlanUseCase> { clearActivePlanUseCase(get()) }
    factory<AdvanceActivePlanDayUseCase> { advanceActivePlanDayUseCase(get()) }
}
