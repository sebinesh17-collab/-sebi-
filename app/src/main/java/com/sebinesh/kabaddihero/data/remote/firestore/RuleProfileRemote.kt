package com.sebinesh.kabaddihero.data.remote.firestore

import com.sebinesh.kabaddihero.core.result.Result
import com.sebinesh.kabaddihero.data.local.DemoDataProvider
import com.sebinesh.kabaddihero.data.model.RuleProfile
import kotlinx.coroutines.tasks.await

class RuleProfileRemote {
    suspend fun getRuleProfiles(): Result<List<RuleProfile>> {
        val col = FirestoreRefs.ruleProfiles() ?: return Result.Success(DemoDataProvider.ruleProfiles)
        return try {
            val snapshot = col.get().await()
            val list = snapshot.toObjects(RuleProfile::class.java)
            if (list.isEmpty()) {
                Result.Success(DemoDataProvider.ruleProfiles)
            } else {
                Result.Success(list)
            }
        } catch (e: Exception) {
            Result.Success(DemoDataProvider.ruleProfiles)
        }
    }
}
