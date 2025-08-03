package com.team.todoktodok.presentation.view.profile.adapter

import com.team.domain.model.Book
import com.team.domain.model.Discussion

sealed class ContentItems(
    val viewType: ViewType,
) {
    data class BookItem(
        val value: Book,
    ) : ContentItems(ViewType.ACTIVATED_BOOKS)

    data class CreatedItem(
        val value: Discussion,
    ) : ContentItems(ViewType.CREATED_DISCUSSION_ROOMS)

    data class JoinedItem(
        val value: Discussion,
    ) : ContentItems(ViewType.JOINED_DISCUSSION_ROOMS)

    enum class ViewType {
        ACTIVATED_BOOKS,
        CREATED_DISCUSSION_ROOMS,
        JOINED_DISCUSSION_ROOMS,
    }
}
