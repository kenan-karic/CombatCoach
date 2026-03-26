package io.aethibo.combatcoach.shared.timer.domain.repository

import io.aethibo.combatcoach.shared.timer.domain.model.TimerSessionState
import kotlinx.coroutines.flow.StateFlow

interface TimerRepository {

    /** Current session state. Observed by the foreground service for notification updates. */
    val session: StateFlow<TimerSessionState>

    /**
     * Starts a new session. If a session with the same [itemId] and [sessionType]
     * is already active this is a no-op (rotation-safe).
     */
    fun startSession(initial: TimerSessionState.Active)

    /** Clears the session back to [TimerSessionState.Idle]. */
    fun clearSession()

    /**
     * Arbitrary state patch. Presenters push local timer state here every tick
     * so the notification stays current.
     */
    fun update(block: TimerSessionState.Active.() -> TimerSessionState.Active)
}
