package com.team.todoktodok.data.network.service

import com.team.domain.model.exception.NetworkResult
import com.team.todoktodok.data.network.request.FcmTokenRequest
import retrofit2.http.Body
import retrofit2.http.PUT

interface NotificationService {
    @PUT("v1/notifications")
    suspend fun saveFcmToken(
        @Body fcmTokenRequest: FcmTokenRequest,
    ): NetworkResult<Unit>
}
