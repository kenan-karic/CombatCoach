package io.aethibo.combatcoach.features.timer.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import io.aethibo.combatcoach.R
import io.aethibo.combatcoach.features.main.MainActivity
import io.aethibo.combatcoach.shared.timer.domain.model.TimerPhase
import io.aethibo.combatcoach.shared.timer.domain.model.TimerSessionState
import io.aethibo.combatcoach.shared.timer.domain.model.TimerSessionType

const val TIMER_NOTIFICATION_ID = 1001
private const val CHANNEL_ID = "timer_channel"
private const val CHANNEL_NAME = "Active Timer"

const val ACTION_PAUSE_RESUME = "io.aethibo.combatcoachex.TIMER_PAUSE_RESUME"
const val ACTION_STOP = "io.aethibo.combatcoachex.TIMER_STOP"
const val ACTION_NEXT = "io.aethibo.combatcoachex.TIMER_NEXT"

class TimerNotificationManager(private val context: Context) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createChannel()
    }

    private fun createChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT,
        ).apply {
            description = "Shows the active workout timer"
            setShowBadge(false)
        }
        notificationManager.createNotificationChannel(channel)
    }

    fun buildNotification(state: TimerSessionState.Active): Notification {
        val contentIntent = PendingIntent.getActivity(
            context, 0,
            Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val stopIntent = servicePendingIntent(ACTION_STOP, 2)
        val nextIntent = servicePendingIntent(ACTION_NEXT, 3)

        val timeLabel = formatSeconds(state.secondsRemaining)
        val phaseLabel = when (state.phase) {
            TimerPhase.WORK -> "Work"
            TimerPhase.REST -> "Rest"
            TimerPhase.COMPLETE -> "Done"
            TimerPhase.IDLE -> ""
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_time)
            .setContentTitle(state.sessionTitle)
            .setContentText("$phaseLabel · $timeLabel · ${state.notificationText}")
            .setContentIntent(contentIntent)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setSilent(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        // Pause makes no sense for strength (set-driven, no running countdown)
        if (state.sessionType != TimerSessionType.STRENGTH) {
            val pauseResumeIntent = servicePendingIntent(ACTION_PAUSE_RESUME, 1)
            val pauseResumeLabel = if (state.isPaused) "Resume" else "Pause"
            val pauseResumeIcon =
                if (state.isPaused) R.drawable.ic_play_circle else R.drawable.ic_pause_circle
            builder.addAction(pauseResumeIcon, pauseResumeLabel, pauseResumeIntent)
        }

        return builder
            .addAction(R.drawable.ic_arrow_right_circle, "Next", nextIntent)
            .addAction(R.drawable.ic_stop_circle, "Stop", stopIntent)
            .build()
    }

    fun updateNotification(notification: Notification) {
        notificationManager.notify(TIMER_NOTIFICATION_ID, notification)
    }

    private fun servicePendingIntent(action: String, requestCode: Int): PendingIntent =
        PendingIntent.getService(
            context, requestCode,
            Intent(context, TimerForegroundService::class.java).setAction(action),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

    private fun formatSeconds(total: Int): String {
        val m = total / 60
        val s = total % 60
        return "%d:%02d".format(m, s)
    }
}
