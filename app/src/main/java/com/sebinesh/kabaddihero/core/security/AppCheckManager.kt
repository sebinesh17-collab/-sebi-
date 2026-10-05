package com.sebinesh.kabaddihero.core.security

import android.content.Context
import com.google.firebase.FirebaseApp
import com.sebinesh.kabaddihero.core.logging.Logger

object AppCheckManager {
    fun initialize(context: Context) {
        try {
            if (FirebaseApp.getApps(context).isNotEmpty()) {
                Logger.i("AppCheckManager", "FirebaseApp initialized and ready")
            }
        } catch (e: Exception) {
            Logger.w("AppCheckManager", "App Check status: ${e.message}")
        }
    }
}
