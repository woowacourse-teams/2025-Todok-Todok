package com.team.ui_xml.create

import android.os.Parcelable
import com.team.core.serialization.SerializationBook
import com.team.core.serialization.SerializationSearchedBook
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
