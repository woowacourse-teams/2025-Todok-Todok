package com.team.todoktodok.presentation.view.discussiondetail

sealed interface DiscussionDetailUiEvent {
    data class ShowComments(
        val discussionId: Long,
    ) : DiscussionDetailUiEvent

    data object AlreadyReportDiscussion : DiscussionDetailUiEvent

    data class UpdateDiscussion(
        val discussionId: Long,
    ) : DiscussionDetailUiEvent

    data class DeleteDiscussion(
        val discussionId: Long,
    ) : DiscussionDetailUiEvent
}
