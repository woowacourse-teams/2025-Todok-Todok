package com.team.todoktodok.presentation.view.discussion.create

import com.team.todoktodok.presentation.view.serialization.SerializationBook

sealed class CreateDiscussionRoomMode {
    data class Create(
        val selectedBook: SerializationBook,
    ) : CreateDiscussionRoomMode()

    data class Edit(
        val discussionRoomId: Int,
    ) : CreateDiscussionRoomMode()
}
