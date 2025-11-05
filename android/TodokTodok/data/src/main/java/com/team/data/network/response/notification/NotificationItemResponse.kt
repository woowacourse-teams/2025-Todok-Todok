package com.team.data.network.response.notification

import com.team.data.core.ext.toLocalDateTime
import com.team.domain.model.notification.Notification
import kotlinx.serialization.Serializable

@Serializable
data class NotificationItemResponse(
    val data: NotificationContentResponse,
    val isRead: Boolean,
    val createdAt: String,
)

fun NotificationItemResponse.toDomain(): Notification =
    Notification(
        id = data.notificationId,
        notificationContent = data.toDomain(),
        isRead = isRead,
        createdAt = createdAt.toLocalDateTime(),
    )
