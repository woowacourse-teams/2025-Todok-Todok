package com.team.todoktodok.data.datasource.notification

import com.team.domain.model.exception.NetworkResult

interface NotificationRemoteDataSource {
    suspend fun saveFcmToken(
        fcmToken: String,
        fId: String,
    ): NetworkResult<Unit>
}
