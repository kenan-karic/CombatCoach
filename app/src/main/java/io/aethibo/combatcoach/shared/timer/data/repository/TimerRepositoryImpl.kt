package io.aethibo.combatcoach.shared.timer.data.repository

import io.aethibo.combatcoach.shared.timer.domain.model.TimerSessionState
import io.aethibo.combatcoach.shared.timer.domain.repository.TimerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TimerRepositoryImpl : TimerRepository {

    private val _session = MutableStateFlow<TimerSessionState>(TimerSessionState.Idle)
    override val session: StateFlow<TimerSessionState> = _session.asStateFlow()

    override fun startSession(initial: TimerSessionState.Active) {
        val current = _session.value
        // Rotation guard: don't restart a session that's already running
        if (current is TimerSessionState.Active &&
            current.itemId == initial.itemId &&
            current.sessionType == initial.sessionType
        ) return
        _session.value = initial
    }

    override fun clearSession() {
        _session.value = TimerSessionState.Idle
    }

    override fun update(block: TimerSessionState.Active.() -> TimerSessionState.Active) {
        val active = _session.value as? TimerSessionState.Active ?: return
        _session.value = active.block()
    }
}
