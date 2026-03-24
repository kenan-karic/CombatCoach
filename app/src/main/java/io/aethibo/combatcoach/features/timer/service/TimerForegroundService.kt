package io.aethibo.combatcoachex.features.timer.domain.service

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.IBinder
import io.aethibo.combatcoach.features.timer.service.ACTION_NEXT
import io.aethibo.combatcoach.features.timer.service.ACTION_PAUSE_RESUME
import io.aethibo.combatcoach.features.timer.service.ACTION_STOP
import io.aethibo.combatcoach.features.timer.service.TIMER_NOTIFICATION_ID
import io.aethibo.combatcoach.features.timer.service.TimerNotificationManager
import io.aethibo.combatcoachex.features.timer.domain.TimerRepository
import io.aethibo.combatcoachex.features.timer.domain.TimerSessionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject

class TimerForegroundService : Service() {

    inner class LocalBinder : Binder() {
        fun getService(): TimerForegroundService = this@TimerForegroundService
    }

    private val binder = LocalBinder()
    private val repository: TimerRepository by inject()
    private val notificationManager: TimerNotificationManager by lazy {
        TimerNotificationManager(applicationContext)
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    var onStopRequested: (() -> Unit)? = null
    var onNextRequested: (() -> Unit)? = null
    var onPauseResumeRequested: (() -> Unit)? = null

    override fun onCreate() {
        super.onCreate()
        observeSession()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_PAUSE_RESUME -> onPauseResumeRequested?.invoke()
            ACTION_STOP -> onStopRequested?.invoke() ?: stopSelf()
            ACTION_NEXT -> onNextRequested?.invoke()
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

    private fun observeSession() {
        repository.session
            .onEach { state ->
                when (state) {
                    is TimerSessionState.Idle -> {
                        stopForeground(STOP_FOREGROUND_REMOVE)
                        stopSelf()
                    }
                    is TimerSessionState.Active -> {
                        val notification = notificationManager.buildNotification(state)
                        startForeground(
                            TIMER_NOTIFICATION_ID,
                            notification,
                            ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC,
                        )
                        notificationManager.updateNotification(notification)
                    }
                }
            }
            .launchIn(scope)
    }
}
