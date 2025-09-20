package com.team.todoktodok.presentation.view.notification.adapter

import com.team.todoktodok.R

sealed class NotificationGroup(
    val viewType: Int,
) {
    data object Information : NotificationGroup(R.layout.item_notification_information)

    data class Notification(
        val notification: com.team.domain.model.notification.Notification,
    ) : NotificationGroup(R.layout.item_notification)
}
