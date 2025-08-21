package com.team.todoktodok.presentation.view.discussiondetail

import com.team.domain.model.Book
import com.team.domain.model.Discussion
import com.team.domain.model.member.Nickname
import com.team.domain.model.member.User
import com.team.todoktodok.presentation.view.discussiondetail.model.DiscussionItemUiState
import java.time.LocalDateTime

data class DiscussionDetailUiState(
    val discussionItemUiState: DiscussionItemUiState,
    val isLoading: Boolean,
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
