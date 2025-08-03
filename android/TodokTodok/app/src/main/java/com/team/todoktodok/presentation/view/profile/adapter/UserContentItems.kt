package com.team.todoktodok.presentation.view.profile.adapter

import com.team.domain.model.Book
import com.team.domain.model.Discussion

sealed class UserContentItems(
    val viewType: ViewType,
) {
    data class BookItem(
        val value: Book,
    ) : UserContentItems(ViewType.ACTIVATED_BOOKS)

    data class CreatedItem(
        val value: Discussion,
    ) : UserContentItems(ViewType.CREATED_DISCUSSION_ROOMS)

    data class JoinedItem(
        val value: Discussion,
    ) : UserContentItems(ViewType.JOINED_DISCUSSION_ROOMS)

    enum class ViewType {
        ACTIVATED_BOOKS,
        CREATED_DISCUSSION_ROOMS,
        JOINED_DISCUSSION_ROOMS,
    }
}
