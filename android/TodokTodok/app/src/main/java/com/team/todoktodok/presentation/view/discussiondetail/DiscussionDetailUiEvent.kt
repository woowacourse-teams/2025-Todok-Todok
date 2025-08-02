package com.team.todoktodok.presentation.view.discussiondetail

sealed interface DiscussionDetailUiEvent {
    data object NavigateUp : DiscussionDetailUiEvent

    data object ShowComments : DiscussionDetailUiEvent

    data class ToggleLikeOnDiscussion(
        val discussionId: Long,
    ) : DiscussionDetailUiEvent

    data class ReportDiscussion(
        val discussionId: Long,
    ) : DiscussionDetailUiEvent

    data class UpdateDiscussion(
        val discussionId: Long,
    ) : DiscussionDetailUiEvent

    data class DeleteDiscussion(
        val discussionId: Long,
    ) : DiscussionDetailUiEvent
}
