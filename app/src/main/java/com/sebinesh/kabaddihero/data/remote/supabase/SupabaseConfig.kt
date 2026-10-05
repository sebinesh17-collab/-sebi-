package com.sebinesh.kabaddihero.data.remote.supabase

import com.sebinesh.kabaddihero.BuildConfig

object SupabaseConfig {
    val projectId: String = try {
        BuildConfig.SUPABASE_PROJECT_ID.ifBlank { "mdmajbpmpzfvjktvfaqj" }
    } catch (_: Exception) {
        "mdmajbpmpzfvjktvfaqj"
    }

    val publishableKey: String = try {
        BuildConfig.SUPABASE_PUBLISHABLE_KEY.ifBlank { "sb_publishable_YN_uMrj3n2935cADtRtrsw_RKjT5RC0" }
    } catch (_: Exception) {
        "sb_publishable_YN_uMrj3n2935cADtRtrsw_RKjT5RC0"
    }

    val url: String = try {
        BuildConfig.SUPABASE_URL.ifBlank { "https://mdmajbpmpzfvjktvfaqj.supabase.co" }
    } catch (_: Exception) {
        "https://mdmajbpmpzfvjktvfaqj.supabase.co"
    }
}
