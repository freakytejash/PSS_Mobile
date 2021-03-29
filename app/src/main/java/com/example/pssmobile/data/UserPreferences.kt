package com.example.pssmobile.data

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(
        context: Context
) {
    private val applicationContext = context.applicationContext
    private val dataStore: DataStore<Preferences>

    init {
        dataStore = applicationContext.createDataStore(
                name = "my_data_store"
        )
    }

    val authToken: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_AUTH]
        }

    suspend fun saveAuthToken(authToken: String) {
        dataStore.edit { preferences ->
            preferences[KEY_AUTH] = authToken
        }
    }

    val zohoauthToken: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_ZOHO_AUTH]
        }

    suspend fun saveZohoAuthToken(zohoauthToken: String) {
        dataStore.edit { preferences ->
            preferences[KEY_ZOHO_AUTH] = zohoauthToken
        }
    }

    val roleId: Flow<Int?>
        get() = dataStore.data.map { preferences ->
            preferences[ROLE_ID]
        }

    suspend fun saveRoleId(roleId: Int) {
        dataStore.edit { preferences ->
            preferences[ROLE_ID] = roleId
        }
    }

    suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        private val KEY_AUTH = preferencesKey<String>("key_auth")
        private val KEY_ZOHO_AUTH = preferencesKey<String>("key_zoho_auth")
        private val ROLE_ID = preferencesKey<Int>("role_id")
    }

}