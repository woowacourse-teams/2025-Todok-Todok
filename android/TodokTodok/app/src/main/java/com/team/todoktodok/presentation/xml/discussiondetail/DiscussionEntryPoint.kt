package com.team.todoktodok.presentation.xml.discussiondetail

import com.team.todoktodok.presentation.xml.discussion.create.SerializationCreateDiscussionRoomMode

sealed interface DiscussionEntryPoint {
    data class Standard(
        val discussionId: Long,
        val mode: SerializationCreateDiscussionRoomMode?,
    ) : DiscussionEntryPoint

    data class FromDeepLink(
        val discussionId: Long,
    ) : DiscussionEntryPoint

    data object Invalid : DiscussionEntryPoint
}
