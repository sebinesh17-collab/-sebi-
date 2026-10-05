package com.sebinesh.kabaddihero.data.remote.fcm

import com.sebinesh.kabaddihero.core.logging.Logger

object FcmTokenManager {
    suspend fun getOrFetchToken(): String? {
        return null
    }

    fun subscribeToDistrict(district: String) {
        Logger.d("FcmTokenManager", "Subscribed locally to $district")
    }

    fun subscribeToMatch(matchId: String) {
        Logger.d("FcmTokenManager", "Subscribed locally to match $matchId")
    }
}
