package com.sebinesh.kabaddihero.data.repository

import com.sebinesh.kabaddihero.core.result.Result
import com.sebinesh.kabaddihero.data.local.DemoDataProvider
import com.sebinesh.kabaddihero.data.model.RuleProfile
import com.sebinesh.kabaddihero.data.remote.firestore.RuleProfileRemote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RuleProfileRepository(
    private val ruleProfileRemote: RuleProfileRemote
) {
    private val _profiles = MutableStateFlow<List<RuleProfile>>(DemoDataProvider.ruleProfiles)
    val profiles: StateFlow<List<RuleProfile>> = _profiles.asStateFlow()

    suspend fun loadProfiles(): List<RuleProfile> {
        val res = ruleProfileRemote.getRuleProfiles()
        return if (res is Result.Success && res.data.isNotEmpty()) {
            _profiles.value = res.data
            res.data
        } else {
            DemoDataProvider.ruleProfiles
        }
    }

    fun getProfileById(id: String): RuleProfile? {
        return _profiles.value.find { it.ruleProfileId == id }
    }
}
