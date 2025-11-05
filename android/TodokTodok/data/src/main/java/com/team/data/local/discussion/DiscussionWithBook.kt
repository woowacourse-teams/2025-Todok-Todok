package com.team.data.local.discussion

import androidx.room.Embedded
import androidx.room.Relation

data class DiscussionWithBook(
    @Embedded val discussionRoomEntity: DiscussionRoomEntity,
    @Relation(
        parentColumn = "book_id",
        entityColumn = "id",
    )
    val bookEntity: BookEntity,
)
