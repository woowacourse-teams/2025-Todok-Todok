package com.team.todoktodok.data.network.request

import kotlinx.serialization.Serializable

@Serializable
data class ModifyProfileRequest(
    val nickname: String,
    val profileMessage: String,
)
