package com.team.todoktodok.presentation.view.discussiondetail

import com.team.domain.model.Discussion

data class DiscussionDetailUiState(
    val discussion: Discussion,
    val isMyDiscussion: Boolean,
    val isLoading: Boolean = false,
) {
    companion object {
        private val INIT_DISCUSSION =
            Discussion(
                id = -1L,
                discussionTitle = "",
                book = Book(-1L, "", "", ""),
                writer = User(-1L, Nickname("동전"), ""),
                createAt = LocalDateTime.of(2000, 1, 1, 1, 1),
                discussionOpinion = "",
                likeCount = 0,
                commentCount = 0,
                isLikedByMe = false,
            )
        val INIT_DISCUSSION_DETAIL_UI_STATE =
            DiscussionDetailUiState(
                discussionItemUiState = DiscussionItemUiState(INIT_DISCUSSION, false),
                false,
            )
    }
}
