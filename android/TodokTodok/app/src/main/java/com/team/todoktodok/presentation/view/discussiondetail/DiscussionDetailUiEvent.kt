package com.team.todoktodok.presentation.view.discussiondetail

import com.team.domain.model.exception.TodokTodokExceptions

sealed interface DiscussionDetailUiEvent {
    data class ShowComments(
        val discussionId: Long,
    ) : DiscussionDetailUiEvent

    data class UpdateDiscussion(
        val discussionId: Long,
    ) : DiscussionDetailUiEvent

    data class DeleteDiscussion(
        val discussionId: Long,
    ) : DiscussionDetailUiEvent

    data class NavigateToProfile(
        val userId: Long,
    ) : DiscussionDetailUiEvent

    data object ShowReportDiscussionSuccessMessage : DiscussionDetailUiEvent

    data class ShowErrorMessage(
        val exceptions: TodokTodokExceptions,
    ) : DiscussionDetailUiEvent
}
