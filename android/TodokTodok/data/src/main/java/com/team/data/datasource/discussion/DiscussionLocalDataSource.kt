package com.team.data.datasource.discussion

import com.team.data.local.discussion.BookEntity
import com.team.data.local.discussion.DiscussionRoomEntity
import com.team.data.local.discussion.DiscussionWithBook

interface DiscussionLocalDataSource {
    suspend fun saveDiscussion(
        discussionEntity: DiscussionRoomEntity,
        bookEntity: BookEntity,
    )

    suspend fun getDiscussion(id: Long): DiscussionWithBook?

    suspend fun hasDiscussion(): Boolean

    suspend fun getBook(id: Long): BookEntity

    suspend fun deleteDiscussion()

    suspend fun getDiscussionCount(): Int

    suspend fun getDiscussions(): List<DiscussionWithBook>
}
