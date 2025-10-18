package com.team.todoktodok.data.network.adapter

import kotlinx.serialization.Serializable

@Serializable
data class ExceptionResponse(
    val code: Int,
    val message: String,
)
