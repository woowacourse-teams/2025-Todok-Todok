package com.team.ui_xml.discussiondetail

import com.team.ui_xml.create.SerializationCreateDiscussionRoomMode

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
