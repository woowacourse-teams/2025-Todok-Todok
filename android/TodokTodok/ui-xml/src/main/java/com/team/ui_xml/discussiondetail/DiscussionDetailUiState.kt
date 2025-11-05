package com.team.ui_xml.discussiondetail

import com.team.domain.model.Discussion

sealed interface DiscussionDetailUiState {
    object Empty : DiscussionDetailUiState

    data class Success(
        val discussion: Discussion,
        val isMyDiscussion: Boolean,
        val isLoading: Boolean = false,
    ) : DiscussionDetailUiState {
        val bookId = discussion.book.id
    }
}
