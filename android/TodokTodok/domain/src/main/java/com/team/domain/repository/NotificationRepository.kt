package com.team.domain.repository

import com.team.domain.model.exception.NetworkResult

interface NotificationRepository {
    suspend fun registerPushNotification(): NetworkResult<Unit>

    suspend fun registerPushNotification(
        token: String,
        fId: String,
    ): NetworkResult<Unit>
}
