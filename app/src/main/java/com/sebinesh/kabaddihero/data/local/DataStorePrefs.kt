package com.sebinesh.kabaddihero.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.sebinesh.kabaddihero.core.constants.AppConstants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = AppConstants.PREFS_NAME)

class DataStorePrefs(private val context: Context) {
    companion object {
        private val FIRST_LAUNCH_KEY = booleanPreferencesKey(AppConstants.KEY_FIRST_LAUNCH)
        private val LANGUAGE_KEY = stringPreferencesKey(AppConstants.KEY_SELECTED_LANG)
        private val DISTRICT_KEY = stringPreferencesKey(AppConstants.KEY_OFFLINE_DISTRICT)
    }

    val isFirstLaunch: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[FIRST_LAUNCH_KEY] ?: true
    }

    suspend fun setFirstLaunchCompleted() {
        context.dataStore.edit { preferences ->
            preferences[FIRST_LAUNCH_KEY] = false
        }
    }

    val selectedLanguage: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[LANGUAGE_KEY] ?: AppConstants.LANG_ENGLISH
    }

    suspend fun setLanguage(lang: String) {
        context.dataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = lang
        }
    }

    val preferredDistrict: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[DISTRICT_KEY] ?: "Madurai"
    }

    suspend fun setPreferredDistrict(district: String) {
        context.dataStore.edit { preferences ->
            preferences[DISTRICT_KEY] = district
        }
    }
}
