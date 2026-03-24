package io.aethibo.combatcoach.features.timer.utils

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import io.aethibo.combatcoachex.features.timer.domain.service.TimerForegroundService

@Composable
fun rememberTimerServiceController(
    onStopRequested: () -> Unit,
    onNextRequested: () -> Unit,
    onPauseResumeRequested: () -> Unit,
): TimerServiceController {
    val context = LocalContext.current
    val controller = remember { TimerServiceController(context) }

    val currentStop by rememberUpdatedState(onStopRequested)
    val currentNext by rememberUpdatedState(onNextRequested)
    val currentPauseResume by rememberUpdatedState(onPauseResumeRequested)

    DisposableEffect(Unit) {
        controller.bind(
            onStopRequested = { currentStop() },
            onNextRequested = { currentNext() },
            onPauseResumeRequested = { currentPauseResume() },
        )
        onDispose { controller.unbind() }
    }

    return controller
}

class TimerServiceController(private val context: Context) {

    private var service: TimerForegroundService? = null
    private var bound by mutableStateOf(false)

    private var pendingStop: (() -> Unit)? = null
    private var pendingNext: (() -> Unit)? = null
    private var pendingPauseResume: (() -> Unit)? = null

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            service = (binder as TimerForegroundService.LocalBinder).getService().also {
                it.onStopRequested = pendingStop
                it.onNextRequested = pendingNext
                it.onPauseResumeRequested = pendingPauseResume
            }
            bound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            service = null
            bound = false
        }
    }

    fun bind(
        onStopRequested: () -> Unit,
        onNextRequested: () -> Unit,
        onPauseResumeRequested: () -> Unit,
    ) {
        pendingStop = onStopRequested
        pendingNext = onNextRequested
        pendingPauseResume = onPauseResumeRequested

        val intent = Intent(context, TimerForegroundService::class.java)
        context.startForegroundService(intent)
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    fun unbind() {
        if (bound) {
            service?.onStopRequested = null
            service?.onNextRequested = null
            service?.onPauseResumeRequested = null
            context.unbindService(connection)
            bound = false
        }
    }

    fun stopService() {
        context.stopService(Intent(context, TimerForegroundService::class.java))
    }
}
