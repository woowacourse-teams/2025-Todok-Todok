package com.team.ui_xml.discussiondetail

import com.team.domain.model.exception.TodokTodokExceptions
import com.team.ui_xml.create.SerializationCreateDiscussionRoomMode

sealed interface DiscussionDetailUiEvent {
    data class UpdateDiscussion(
        val discussionId: Long,
    ) : DiscussionDetailUiEvent

    data class DeleteDiscussion(
        val discussionId: Long,
    ) : DiscussionDetailUiEvent

    data class NavigateToProfile(
        val userId: Long,
    ) : DiscussionDetailUiEvent

    data class NavigateToDiscussionsWithResult(
        val mode: SerializationCreateDiscussionRoomMode?,
    ) : DiscussionDetailUiEvent

    data class NavigateToBookDiscussions(
        val bookId: Long,
    ) : DiscussionDetailUiEvent

    data object ShowReportDiscussionSuccessMessage : DiscussionDetailUiEvent

    data object ReloadedDiscussion : DiscussionDetailUiEvent

    data class ShowErrorMessage(
        val exceptions: TodokTodokExceptions,
    ) : DiscussionDetailUiEvent

    data class NotFoundDiscussion(
        val exceptions: TodokTodokExceptions,
    ) : DiscussionDetailUiEvent

    data class Unauthorized(
        val exceptions: TodokTodokExceptions,
    ) : DiscussionDetailUiEvent

    data class ShareDiscussion(
        val discussionId: Long,
        val discussionTitle: String,
    ) : DiscussionDetailUiEvent
}
