package com.team.todoktodok.data.network.response.notification

data class NotificationResponse(
    val notReadCount: Int,
    val notifications: List<NotificationItemResponse>,
)
