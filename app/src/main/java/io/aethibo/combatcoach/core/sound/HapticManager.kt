package io.aethibo.combatcoach.core.sound

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

/**
 * Wraps [Vibrator] / [VibratorManager] to provide named haptic patterns.
 *
 * Supports API 26+ [VibrationEffect]; silently no-ops on older devices.
 * Like [SoundManager], callers pass [enabled] per call so this class
 * remains stateless with respect to user prefs.
 */
class HapticManager(context: Context) {

    private val vibrator: Vibrator =
        (context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator

    // ── Public patterns ────────────────────────────────────────────────────

    /**
     * Single firm pulse — used for round start.
     * Pattern: 0 ms delay → 80 ms ON → 40 ms OFF → 80 ms ON
     */
    fun roundStart(enabled: Boolean) {
        if (!enabled) return
        vibratePattern(longArrayOf(0, 80, 40, 80), amplitudes = intArrayOf(0, 200, 0, 200))
    }

    /**
     * Double short pulse — used for round end.
     * Pattern: 0 ms → 60 ms ON → 60 ms OFF → 60 ms ON
     */
    fun roundEnd(enabled: Boolean) {
        if (!enabled) return
        vibratePattern(longArrayOf(0, 60, 60, 60), amplitudes = intArrayOf(0, 180, 0, 180))
    }

    /**
     * Single light tick — used for each countdown beep (3, 2, 1).
     * Kept short so it doesn't bleed into the next second.
     */
    fun countdownTick(enabled: Boolean) {
        if (!enabled) return
        vibrateSingle(durationMs = 30, amplitude = 120)
    }

    // ── Private helpers ────────────────────────────────────────────────────

    private fun vibrateSingle(durationMs: Long, amplitude: Int) {
        if (!vibrator.hasVibrator()) return
        val effect = VibrationEffect.createOneShot(durationMs, amplitude)
        vibrator.vibrate(effect)
    }

    private fun vibratePattern(timings: LongArray, amplitudes: IntArray) {
        if (!vibrator.hasVibrator()) return
        val effect = VibrationEffect.createWaveform(timings, amplitudes, /* repeat = */ -1)
        vibrator.vibrate(effect)
    }
}