package com.community.swaphub.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

class PreferencesManager(private val context: Context) {
    companion object {
        private val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token")
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
    }
    
    val authToken: Flow<String?> = context.dataStore.data.map { it[AUTH_TOKEN_KEY] }
    val userId: Flow<String?> = context.dataStore.data.map { it[USER_ID_KEY] }
    val userEmail: Flow<String?> = context.dataStore.data.map { it[USER_EMAIL_KEY] }
    
    suspend fun saveAuthToken(token: String) {
        context.dataStore.edit { it[AUTH_TOKEN_KEY] = token }
    }
    
    suspend fun saveUserId(userId: String) {
        context.dataStore.edit { it[USER_ID_KEY] = userId }
    }
    
    suspend fun saveUserEmail(email: String) {
        context.dataStore.edit { it[USER_EMAIL_KEY] = email }
    }
    
    suspend fun clearAuth() {
        context.dataStore.edit {
            it.remove(AUTH_TOKEN_KEY)
            it.remove(USER_ID_KEY)
            it.remove(USER_EMAIL_KEY)
        }
    }
    
    suspend fun getAuthTokenSync(): String? {
        return kotlinx.coroutines.runBlocking {
            context.dataStore.data.map { it[AUTH_TOKEN_KEY] }.first()
        }
    }
}

