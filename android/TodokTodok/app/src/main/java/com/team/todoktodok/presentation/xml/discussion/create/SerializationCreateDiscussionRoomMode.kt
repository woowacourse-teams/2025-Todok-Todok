package com.team.todoktodok.presentation.xml.discussion.create

import android.os.Parcelable
import com.team.todoktodok.presentation.xml.serialization.SerializationBook
import com.team.todoktodok.presentation.xml.serialization.SerializationSearchedBook
import kotlinx.parcelize.Parcelize

sealed class SerializationCreateDiscussionRoomMode : Parcelable {
    @Parcelize
    data class Create(
        val selectedBook: SerializationSearchedBook,
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
