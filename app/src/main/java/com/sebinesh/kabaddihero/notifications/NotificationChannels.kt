package com.sebinesh.kabaddihero.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

object NotificationChannels {
    const val CHANNEL_TOURNAMENTS = "channel_tournaments"
    const val CHANNEL_LIVE_MATCHES = "channel_live_matches"
    const val CHANNEL_SYSTEM = "channel_system"

    fun createChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val tourneyChannel = NotificationChannel(
                CHANNEL_TOURNAMENTS,
                "Tournaments",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "New tournaments, registration deadlines and updates"
            }

            val liveMatchChannel = NotificationChannel(
                CHANNEL_LIVE_MATCHES,
                "Live Matches",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Live match scores, raider alerts, and finish results"
            }

            val systemChannel = NotificationChannel(
                CHANNEL_SYSTEM,
                "Announcements",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "App updates and admin notices"
            }

            manager.createNotificationChannels(listOf(tourneyChannel, liveMatchChannel, systemChannel))
        }
    }
}
