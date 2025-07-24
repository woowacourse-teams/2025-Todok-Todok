package com.example.todoktodok.data.network.request

import kotlinx.serialization.Serializable

@Serializable
data class SaveBookRequest(
    val id: Long,
)
