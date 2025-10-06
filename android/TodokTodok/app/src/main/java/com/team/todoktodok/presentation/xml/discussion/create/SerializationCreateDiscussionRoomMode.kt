package com.team.todoktodok.presentation.xml.discussion.create

import android.os.Parcelable
import com.team.todoktodok.presentation.xml.serialization.SerializationBook
import kotlinx.parcelize.Parcelize

sealed interface SerializationCreateDiscussionRoomMode : Parcelable {
    @Parcelize
    data class Create(
        val selectedBook: SerializationBook,
    ) : SerializationCreateDiscussionRoomMode
//
//    @Parcelize
//    data class Edit(
//        val discussionRoomId: Long,
//    ) : SerializationCreateDiscussionRoomMode
//
//    @Parcelize
//    data class Draft(
//        val selectedBook: SerializationBook,
//    ) : SerializationCreateDiscussionRoomMode
}
