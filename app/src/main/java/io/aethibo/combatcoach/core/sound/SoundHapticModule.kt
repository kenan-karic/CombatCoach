package io.aethibo.combatcoach.core.sound

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Koin module for audio and haptic services.
 *
 * Both managers are singletons: SoundPool and Vibrator are expensive to
 * create and must live for the app lifetime. Call [SoundManager.release]
 * (via [TimerSoundCoordinator.release]) in your Application.onTerminate
 * or when the timer screen's DisposableEffect fires.
 */
val soundHapticModule = module {
    single { SoundManager(androidContext()) }

    single { HapticManager(androidContext()) }

    single {
        TimerSoundCoordinator(
            soundManager = get(),
            hapticManager = get(),
        )
    }
}
