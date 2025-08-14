package com.team.todoktodok.data.datasource.token

import android.content.Context
import androidx.datastore.core.DataStore
import com.team.todoktodok.data.database.UserSetting
import com.team.todoktodok.data.database.dataStore
import kotlinx.coroutines.flow.first

class TokenDataSource(
    private val context: Context,
) {
    private val dataStore: DataStore<UserSetting>
        get() = context.dataStore

    suspend fun getAccessToken(): String = dataStore.data.first().accessToken

    suspend fun getMemberId(): Long = dataStore.data.first().memberId

    suspend fun saveToken(
        accessToken: String,
        refreshToken: String = TEMPORARY_TOKEN,
        memberId: Long = TEMPORARY_MEMBER_ID,
    ) {
        dataStore.updateData {
            it.copy(
                accessToken,
                "", // 리프레시 토큰 구현
                memberId,
            )
        }
    }

    companion object {
        private const val TEMPORARY_TOKEN = ""
        private const val TEMPORARY_MEMBER_ID = -1L
    }
}
