package com.team.todoktodok.data.network.response.notification

import com.team.domain.model.notification.Notification
import com.team.todoktodok.data.core.ext.toLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class NotificationItemResponse(
    val notificationId: Long,
    val data: NotificationContentResponse,
    val isRead: Boolean,
    val createAt: String,
)

fun NotificationItemResponse.toDomain(): Notification =
    Notification(
        id = notificationId,
        notificationContent = data.toDomain(),
        isRead = isRead,
        createdAt = createAt.toLocalDateTime(),
    )