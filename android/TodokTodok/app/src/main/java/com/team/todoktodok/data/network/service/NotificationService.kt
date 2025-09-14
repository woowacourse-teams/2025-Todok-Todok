package com.team.todoktodok.data.network.service

import com.team.domain.model.exception.NetworkResult
import com.team.todoktodok.data.network.request.FcmTokenRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface NotificationService {
    @POST("v1/notificationTokens")
    suspend fun saveFcmToken(
        @Body fcmTokenRequest: FcmTokenRequest,
    ): NetworkResult<Unit>
}
