package com.team.todoktodok.presentation.view.profile.adapter

import com.team.domain.model.Book
import com.team.domain.model.Discussion

sealed class UserContentItems(
    val viewType: ViewType,
) {
    data class BookItem(
        val value: Book,
    ) : UserContentItems(ViewType.VIEW_TYPE_ACTIVATED_BOOKS)

    data class CreatedItem(
        val value: Discussion,
    ) : UserContentItems(ViewType.VIEW_TYPE_CREATED_DISCUSSION_ROOMS)

    data class JoinedItem(
        val value: Discussion,
    ) : UserContentItems(ViewType.VIEW_TYPE_JOINED_DISCUSSION_ROOMS)

    enum class ViewType {
        VIEW_TYPE_ACTIVATED_BOOKS,
        VIEW_TYPE_CREATED_DISCUSSION_ROOMS,
        VIEW_TYPE_JOINED_DISCUSSION_ROOMS,
    }
}
