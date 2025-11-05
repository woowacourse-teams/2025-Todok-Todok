package com.team.data.network.service

import com.team.data.network.request.FcmTokenRequest
import com.team.data.network.response.notification.ExistNotificationResponse
import com.team.data.network.response.notification.NotificationResponse
import com.team.domain.model.exception.NetworkResult
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface NotificationService {
    @POST("v1/notificationTokens")
    suspend fun saveFcmToken(
        @Body fcmTokenRequest: FcmTokenRequest,
    ): NetworkResult<Unit>

    @GET("v1/notifications")
    suspend fun getNotifications(): NetworkResult<NotificationResponse>

    @PATCH("v1/notifications/{notificationId}/read")
    suspend fun patchNotifications(
        @Path(value = "notificationId") notificationId: Long,
    ): NetworkResult<Unit>

    @DELETE("v1/notifications/{notificationId}")
    suspend fun deleteNotifications(
        @Path(value = "notificationId") notificationId: Long,
    ): NetworkResult<Unit>

    @GET("v1/notifications/unread/exists")
    suspend fun getUnreadNotificationsCount(): NetworkResult<ExistNotificationResponse>
}
