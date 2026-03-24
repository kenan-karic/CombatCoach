package io.aethibo.combatcoach.core.sound

import io.aethibo.combatcoach.shared.user.domain.model.UserPrefs

/**
 * Coordinates [SoundManager] and [HapticManager] in response to timer events.
 *
 * This is the single call-site for all audio/haptic feedback — the timer
 * presenter/ViewModel calls these methods directly; neither manager is
 * referenced outside this class.
 *
 * UserPrefs are passed per-call so this class never holds stale state.
 */
class TimerSoundCoordinator(
    private val soundManager: SoundManager,
    private val hapticManager: HapticManager,
) {

    // ── Timer lifecycle ────────────────────────────────────────────────────

    /**
     * Called when a new round begins.
     */
    fun onRoundStart(prefs: UserPrefs) {
        soundManager.play(SoundManager.SoundEffect.ROUND_START, enabled = prefs.soundEnabled)
        hapticManager.roundStart(enabled = prefs.vibrationEnabled)
    }

    /**
     * Called when a round ends (work period over, rest begins — or final round over).
     */
    fun onRoundEnd(prefs: UserPrefs) {
        soundManager.play(SoundManager.SoundEffect.ROUND_END, enabled = prefs.soundEnabled)
        hapticManager.roundEnd(enabled = prefs.vibrationEnabled)
    }

    // ── Countdown beeps ────────────────────────────────────────────────────

    /**
     * Called once per second when [secondsRemaining] is 3, 2, or 1.
     *
     * - Seconds 3 & 2 → low beep + light haptic tick
     * - Second 1       → high beep + light haptic tick (signals imminent end)
     *
     * All audio/haptic is skipped when the respective pref is disabled.
     */
    fun onCountdownTick(secondsRemaining: Int, prefs: UserPrefs) {
        if (secondsRemaining !in 1..3) return

        val effect = if (secondsRemaining == 1) {
            SoundManager.SoundEffect.BEEP_HIGH
        } else {
            SoundManager.SoundEffect.BEEP_LOW
        }

        soundManager.play(effect, enabled = prefs.soundEnabled && prefs.countdownBeeps)
        hapticManager.countdownTick(enabled = prefs.vibrationEnabled && prefs.countdownBeeps)
    }

    // ── Cleanup ────────────────────────────────────────────────────────────

    /**
     * Forward to [SoundManager.release] when the hosting component is destroyed.
     */
    fun release() {
        soundManager.release()
    }
}
