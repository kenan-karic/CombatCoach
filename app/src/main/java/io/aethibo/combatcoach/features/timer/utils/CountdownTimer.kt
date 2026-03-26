package io.aethibo.combatcoach.features.timer.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CountdownTimer(
    private val onTick: (secondsRemaining: Int) -> Unit,
    private val onComplete: () -> Unit,
) {
    private var job: Job? = null

    fun start(scope: CoroutineScope, durationSeconds: Int) {
        job?.cancel()
        var remaining = durationSeconds
        job = scope.launch {
            while (remaining > 0) {
                delay(1_000L)
                remaining--
                onTick(remaining)
            }
            onComplete()
        }
    }

    fun cancel() {
        job?.cancel()
        job = null
    }

    fun isRunning(): Boolean = job?.isActive == true
}
