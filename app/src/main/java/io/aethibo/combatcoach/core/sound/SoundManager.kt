package io.aethibo.combatcoach.core.sound

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import io.aethibo.combatcoach.R

/**
 * Manages short, low-latency sound effects via [SoundPool].
 *
 * Lifecycle: call [release] when the host (Activity/Service) is destroyed.
 * All play calls are no-ops if the user has disabled sound in their prefs —
 * the caller is responsible for passing [enabled] per call so the manager
 * stays stateless with respect to user prefs.
 */
class SoundManager(context: Context) {

    enum class SoundEffect {
        ROUND_START,   // bell / whistle at round start
        ROUND_END,     // bell at round end
        BEEP_HIGH,     // 3-2-1 final beep (higher pitch — "1")
        BEEP_LOW,      // 3-2-1 regular beep ("3", "2")
    }

    private val audioAttributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_GAME)
        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        .build()

    private val pool: SoundPool = SoundPool.Builder()
        .setMaxStreams(4)
        .setAudioAttributes(audioAttributes)
        .build()

    // Map enum → SoundPool stream id (loaded asynchronously)
    private val soundIds = mutableMapOf<SoundEffect, Int>()

    init {
        // Load raw resources. Add the matching files under res/raw/:
        //   round_start.wav, round_end.wav, beep_high.wav, beep_low.wav
        soundIds[SoundEffect.ROUND_START] = pool.load(context, R.raw.round_start, 1)
        soundIds[SoundEffect.ROUND_END] = pool.load(context, R.raw.round_end, 1)
        soundIds[SoundEffect.BEEP_HIGH] = pool.load(context, R.raw.beep_high, 1)
        soundIds[SoundEffect.BEEP_LOW] = pool.load(context, R.raw.beep_low, 1)
    }

    /**
     * Play [effect] at full volume on both channels.
     *
     * @param enabled Pass `userPrefs.soundEnabled` — play is skipped when false.
     * @param volume  0.0–1.0, defaults to full.
     */
    fun play(effect: SoundEffect, enabled: Boolean, volume: Float = 1f) {
        if (!enabled) return
        val id = soundIds[effect] ?: return
        pool.play(
            /* soundID    = */ id,
            /* leftVolume = */ volume,
            /* rightVolume= */ volume,
            /* priority   = */ 1,
            /* loop        = */ 0,
            /* rate        = */ 1f,
        )
    }

    /** Must be called when the owner is destroyed to free native resources. */
    fun release() {
        pool.release()
    }
}
