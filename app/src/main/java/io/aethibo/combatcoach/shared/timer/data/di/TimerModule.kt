package io.aethibo.combatcoach.shared.timer.data.di

import io.aethibo.combatcoach.features.timer.service.TimerNotificationManager
import io.aethibo.combatcoach.shared.timer.data.repository.TimerRepositoryImpl
import io.aethibo.combatcoach.shared.timer.domain.repository.TimerRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val timerModule = module {
    single<TimerRepository> { TimerRepositoryImpl() }
    single { TimerNotificationManager(androidContext()) }
}
