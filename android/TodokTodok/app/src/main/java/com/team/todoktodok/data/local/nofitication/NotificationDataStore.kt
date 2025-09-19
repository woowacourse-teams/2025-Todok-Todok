package com.team.todoktodok.data.local.nofitication

interface NotificationDataStore {
    suspend fun saveFcmToken(token: String)

    suspend fun saveFId(id: String)

    suspend fun getFcmToken(): String?

    suspend fun getFId(): String?
}
