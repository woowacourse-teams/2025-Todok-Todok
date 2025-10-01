package com.team.todoktodok.presentation.xml.discussiondetail

import com.team.domain.model.exception.TodokTodokExceptions
import com.team.todoktodok.presentation.xml.discussion.create.SerializationCreateDiscussionRoomMode
import com.team.todoktodok.presentation.xml.serialization.SerializationDiscussion

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
        val discussion: SerializationDiscussion,
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
}
