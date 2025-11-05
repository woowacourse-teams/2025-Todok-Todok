package com.team.data.local.notification

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.flow.first

class DefaultNotificationDataStore(
    private val context: Context,
) : NotificationDataStore {
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

    override suspend fun deletePushNotification() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    override suspend fun allowedNotification(isAllowed: Boolean) {
        dataStore.edit { preferences ->
            preferences[KEY_NOTIFICATION_REJECT] = isAllowed
        }
    }

    override suspend fun getIsNotificationAllowed(): Boolean? = dataStore.data.first()[KEY_NOTIFICATION_REJECT]

    companion object {
        private const val DATABASE_NAME: String = "notification"
        private val KEY_FCM_TOKEN = stringPreferencesKey("fcm_token")
        private val KEY_F_ID = stringPreferencesKey("f_id")

        private val KEY_NOTIFICATION_REJECT = booleanPreferencesKey("notification_reject")
    }
}
