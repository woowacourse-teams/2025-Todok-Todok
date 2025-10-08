package com.team.todoktodok.data.datasource.discussion

import com.team.todoktodok.data.local.discussion.BookEntity
import com.team.todoktodok.data.local.discussion.DiscussionRoomEntity
import com.team.todoktodok.data.local.discussion.DiscussionWithBook

interface DiscussionLocalDataSource {
    suspend fun saveDiscussion(
        discussionEntity: DiscussionRoomEntity,
        bookEntity: BookEntity,
    )

    suspend fun getDiscussion(): DiscussionWithBook?

    suspend fun hasDiscussion(): Boolean

    suspend fun getBook(): BookEntity

    suspend fun deleteDiscussion()

    suspend fun getDiscussionCount(): Int
}
