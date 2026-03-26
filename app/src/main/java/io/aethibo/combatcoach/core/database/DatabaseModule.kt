package io.aethibo.combatcoach.core.database

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single { buildDatabase(androidContext()) }
}
