package com.team.todoktodok.presentation.view.discussion.create

import android.os.Parcelable
import com.team.todoktodok.presentation.view.serialization.SerializationBook
import kotlinx.parcelize.Parcelize

sealed class SerializationCreateDiscussionRoomMode : Parcelable {
    @Parcelize
    data class Create(
        val selectedBook: SerializationBook,
    ) : SerializationCreateDiscussionRoomMode()

    @Parcelize
    data class Edit(
        val discussionRoomId: Long,
    ) : SerializationCreateDiscussionRoomMode()

    @Parcelize
    data class Draft(
        val selectedBook: SerializationBook,
    ) : SerializationCreateDiscussionRoomMode()
}
