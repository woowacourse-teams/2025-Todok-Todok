package com.team.todoktodok.data.network.response.notification

import kotlinx.serialization.Serializable

@Serializable
data class NotificationResponse(
    val unreadCount: Int,
    val notifications: List<NotificationItemResponse>,
)
