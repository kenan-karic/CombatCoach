package io.aethibo.combatcoach

import android.app.Application
import io.aethibo.combatcoach.core.database.databaseModule
import io.aethibo.combatcoach.shared.combo.data.di.comboModule
import io.aethibo.combatcoach.shared.log.data.di.workoutLogModule
import io.aethibo.combatcoach.shared.plan.data.di.planModule
import io.aethibo.combatcoach.shared.user.data.di.userPrefsModule
import io.aethibo.combatcoach.shared.workout.data.di.workoutModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import timber.log.Timber

class CombatCoachApplication : Application(), KoinComponent,
    CoroutineScope by CoroutineScope(SupervisorJob() + Dispatchers.IO) {
    override fun onCreate() {
        super.onCreate()

        setupKoin()
        setupTimberLogger()
    }

    private fun setupKoin() {
        startKoin {
            androidLogger()
            androidContext(this@CombatCoachApplication)
            modules(
                databaseModule,
                userPrefsModule,
                workoutModule,
                comboModule,
                planModule,
                workoutLogModule
            )
        }
    }

    private fun setupTimberLogger() {
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }
}
