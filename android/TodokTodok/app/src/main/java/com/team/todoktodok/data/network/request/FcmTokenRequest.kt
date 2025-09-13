package com.team.todoktodok.data.network.request

import kotlinx.serialization.SerialName

data class FcmTokenRequest(
    @SerialName("token")
    val fcmToken: String,
    @SerialName("fid")
    val fId: String,
)
