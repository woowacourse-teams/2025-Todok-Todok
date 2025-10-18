package com.team.todoktodok.data.network.response.book

import com.team.domain.model.book.SearchedBooks
import com.team.domain.model.book.SearchedBooksResult
import com.team.domain.model.book.SearchedBooksResult.Companion.SearchedBooksResult
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

fun SearchedBookResultResponse.toDomain(): SearchedBooksResult =
    SearchedBooksResult(
        books = SearchedBooks(items.map { it.toDomain() }),
        hasNext = pageInfo.hasNext,
        totalSize = totalSize,
    )
