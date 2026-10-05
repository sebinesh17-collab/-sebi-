package com.sebinesh.kabaddihero.domain.rules

import com.sebinesh.kabaddihero.data.local.DemoDataProvider
import com.sebinesh.kabaddihero.data.model.RuleProfile

object RuleProfileRegistry {
    private val profiles = mutableMapOf<String, RuleProfile>()

    init {
        DemoDataProvider.ruleProfiles.forEach {
            profiles[it.ruleProfileId] = it
        }
    }

    fun getProfile(id: String): RuleProfile {
        return profiles[id] ?: DemoDataProvider.ruleProfiles.first()
    }

    fun getAllProfiles(): List<RuleProfile> = profiles.values.toList()

    fun registerProfile(profile: RuleProfile) {
        profiles[profile.ruleProfileId] = profile
    }
}
