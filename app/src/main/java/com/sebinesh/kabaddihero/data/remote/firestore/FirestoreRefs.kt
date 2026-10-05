package com.sebinesh.kabaddihero.data.remote.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.sebinesh.kabaddihero.core.constants.AppConstants

object FirestoreRefs {
    private val db: FirebaseFirestore? by lazy {
        try {
            FirebaseFirestore.getInstance()
        } catch (e: Exception) {
            null
        }
    }

    fun isAvailable(): Boolean = db != null

    fun users() = db?.collection(AppConstants.COLL_USERS)
    fun tournaments() = db?.collection(AppConstants.COLL_TOURNAMENTS)
    fun teams() = db?.collection(AppConstants.COLL_TEAMS)
    fun players() = db?.collection(AppConstants.COLL_PLAYERS)
    fun registrations() = db?.collection(AppConstants.COLL_REGISTRATIONS)
    fun matches() = db?.collection(AppConstants.COLL_MATCHES)
    fun matchEvents() = db?.collection(AppConstants.COLL_MATCH_EVENTS)
    fun reports() = db?.collection(AppConstants.COLL_REPORTS)
    fun ruleProfiles() = db?.collection(AppConstants.COLL_RULE_PROFILES)
    fun auditLogs() = db?.collection(AppConstants.COLL_AUDIT_LOGS)
    fun savedTournaments() = db?.collection(AppConstants.COLL_SAVED)
}
