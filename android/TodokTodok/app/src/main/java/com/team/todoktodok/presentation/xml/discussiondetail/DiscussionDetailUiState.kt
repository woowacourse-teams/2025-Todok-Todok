package com.team.todoktodok.presentation.xml.discussiondetail

import com.team.domain.model.Discussion
import com.team.domain.model.exception.TodokTodokExceptions

sealed interface DiscussionDetailUiState {
    object Loading : DiscussionDetailUiState

    data class Success(
        val discussion: Discussion,
        val isMyDiscussion: Boolean,
        val isLoading: Boolean = false,
    ) : DiscussionDetailUiState {
        val bookId = discussion.book.id
    }

    data class Failure(
        val exception: TodokTodokExceptions,
    ) : DiscussionDetailUiState
}
