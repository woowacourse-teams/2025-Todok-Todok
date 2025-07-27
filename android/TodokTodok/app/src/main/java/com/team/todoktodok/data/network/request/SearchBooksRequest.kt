package com.team.todoktodok.data.network.request

import kotlinx.serialization.Serializable

@Serializable
data class SearchBooksRequest(
    val searchInput: String,
)
