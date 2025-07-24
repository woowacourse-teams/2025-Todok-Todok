package com.example.todoktodok.data.network.request

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
)
