package com.sebinesh.kabaddihero.data.remote.ai

import com.sebinesh.kabaddihero.core.logging.Logger

object FirebaseAiClient {
    fun isAvailable(): Boolean {
        return try {
            Class.forName("com.google.firebase.ai.FirebaseAI")
            true
        } catch (e: Throwable) {
            false
        }
    }
}
