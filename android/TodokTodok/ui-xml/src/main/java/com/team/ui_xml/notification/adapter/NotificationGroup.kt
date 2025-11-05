package com.team.ui_xml.notification.adapter

import com.team.ui_xml.R

sealed class NotificationGroup(
    val viewType: Int,
) {
    data object Information : NotificationGroup(R.layout.item_notification_information)

    data class Count(
        val count: Int,
    ) : NotificationGroup(R.layout.item_notification_count)

    data class Notification(
        val notification: com.team.domain.model.notification.Notification,
    ) : NotificationGroup(R.layout.item_notification)
}
