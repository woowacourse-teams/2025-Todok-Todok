package com.team.todoktodok.data.network.response

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val refreshToken: String?,
)
