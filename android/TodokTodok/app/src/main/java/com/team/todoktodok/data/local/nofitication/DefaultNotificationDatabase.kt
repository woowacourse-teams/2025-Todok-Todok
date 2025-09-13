package com.team.todoktodok.data.local.nofitication

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.flow.first

class DefaultNotificationDatabase(
    private val context: Context,
) : NotificationDatabase {
    private val dataStore: DataStore<Preferences> =
        PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile(DATABASE_NAME)
        }

    override suspend fun saveFcmToken(token: String) {
        dataStore.edit { preferences ->
            preferences[KEY_FCM_TOKEN] = token
        }
    }

    override suspend fun saveFId(id: String) {
        dataStore.edit { preferences ->
            preferences[KEY_F_ID] = id
        }
    }

    override suspend fun getFcmToken(): String? = dataStore.data.first()[KEY_FCM_TOKEN]

    override suspend fun getFId(): String? = dataStore.data.first()[KEY_F_ID]

    companion object {
        private const val DATABASE_NAME: String = "notification"
        private val KEY_FCM_TOKEN = stringPreferencesKey("fcm_token")
        private val KEY_F_ID = stringPreferencesKey("f_id")
    }
}
