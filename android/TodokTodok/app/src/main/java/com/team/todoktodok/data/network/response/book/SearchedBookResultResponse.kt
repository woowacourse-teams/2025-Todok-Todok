package com.team.todoktodok.data.network.response.book

import com.team.todoktodok.data.network.response.latest.PageInfoResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchedBookResultResponse(
    @SerialName("items")
    val items: List<SearchedBookResponse>,
    @SerialName("pageInfo")
    val pageInfo: PageInfoResponse,
    @SerialName("totalSize")
    val totalSize: Int,
)
