package com.sebinesh.kabaddihero

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.sebinesh.kabaddihero.core.di.ServiceLocator
import com.sebinesh.kabaddihero.core.logging.Logger
import com.sebinesh.kabaddihero.core.security.AppCheckManager
import com.sebinesh.kabaddihero.notifications.NotificationChannels

class KabaddiHeroApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Logger.i("KabaddiHeroApp", "Initializing Kabaddi Hero - by Sebinesh")

        try {
            if (FirebaseApp.getApps(this).isEmpty()) {
                val options = FirebaseOptions.Builder()
                    .setApiKey("AIzaSyAJBEA7b5I_VO6HgRJls9VKcSej45Sbfm0")
                    .setApplicationId("1:11892887511:web:690c253cce92a8b8434592")
                    .setProjectId("sebi-c5b8d")
                    .setStorageBucket("sebi-c5b8d.firebasestorage.app")
                    .setGcmSenderId("11892887511")
                    .build()
                FirebaseApp.initializeApp(this, options)
                Logger.i("KabaddiHeroApp", "Firebase initialized for project sebi-c5b8d")
            }
        } catch (e: Exception) {
            Logger.w("KabaddiHeroApp", "Firebase initialization check: ${e.message}")
        }

        ServiceLocator.initialize(this)
        AppCheckManager.initialize(this)
        NotificationChannels.createChannels(this)
    }
}
