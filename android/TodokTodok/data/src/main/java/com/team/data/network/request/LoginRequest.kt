package com.team.data.network.request

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val googleIdToken: String,
)
