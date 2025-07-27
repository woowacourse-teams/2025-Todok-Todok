package com.team.todoktodok.presentation.view.discussion.discussions

sealed interface DiscussionUiEvent {
    data class NavigateToDiscussionDetail(
        val discussionId: Long,
    ) : DiscussionUiEvent

    data object NavigateToAddDiscussion : DiscussionUiEvent
}
