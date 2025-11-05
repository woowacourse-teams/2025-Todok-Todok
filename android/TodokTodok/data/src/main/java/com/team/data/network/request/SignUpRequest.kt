package com.team.data.network.request

import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(
    val nickname: String,
    val googleIdToken: String,
)
