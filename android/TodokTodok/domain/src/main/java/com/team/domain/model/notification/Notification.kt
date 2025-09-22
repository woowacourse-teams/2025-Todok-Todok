package com.team.domain.model.notification

import java.time.LocalDateTime

data class Notification(
    val id: Long,
    val notificationContent: NotificationContent,
    val isRead: Boolean,
    val createdAt: LocalDateTime,
)
