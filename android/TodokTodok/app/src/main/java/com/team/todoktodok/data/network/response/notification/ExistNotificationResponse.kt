package com.team.todoktodok.data.network.response.notification

import kotlinx.serialization.Serializable

@Serializable
data class ExistNotificationResponse(
    val exist: Boolean,
)
