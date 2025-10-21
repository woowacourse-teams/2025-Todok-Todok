package com.team.todoktodok.data.datasource.token

import android.content.Context
import androidx.datastore.core.DataStore
import com.team.todoktodok.data.database.UserSetting
import com.team.todoktodok.data.database.dataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class TokenLocalDataSource
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) : TokenDataSource {
        private val dataStore: DataStore<UserSetting>
            get() = context.dataStore

        override suspend fun getAccessToken(): String = dataStore.data.first().accessToken

        override suspend fun getRefreshToken(): String = dataStore.data.first().refreshToken

        override suspend fun getMemberId(): Long = dataStore.data.first().memberId

        override suspend fun saveAccessToken(accessToken: String) {
            dataStore.updateData { it.copy(accessToken = accessToken) }
        }

        override suspend fun saveToken(
            accessToken: String,
            refreshToken: String,
        ) {
            dataStore.updateData {
                it.copy(
                    accessToken,
                    refreshToken,
                )
            }
        }

        override suspend fun saveSetting(
            accessToken: String,
            refreshToken: String,
            memberId: Long,
        ) {
            dataStore.updateData {
                it.copy(
                    accessToken,
                    refreshToken,
                    memberId,
                )
            }
        }

        override suspend fun clear() {
            dataStore.updateData { UserSetting() }
        }
    }
