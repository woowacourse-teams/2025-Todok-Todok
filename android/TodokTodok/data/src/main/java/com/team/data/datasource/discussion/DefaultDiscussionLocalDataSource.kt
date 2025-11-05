package com.team.data.datasource.discussion

import com.team.data.local.discussion.BookEntity
import com.team.data.local.discussion.DiscussionDao
import com.team.data.local.discussion.DiscussionRoomEntity
import com.team.data.local.discussion.DiscussionWithBook
import javax.inject.Inject

class DefaultDiscussionLocalDataSource
    @Inject
    constructor(
        private val dao: DiscussionDao,
    ) : DiscussionLocalDataSource {
        override suspend fun saveDiscussion(
            discussionEntity: DiscussionRoomEntity,
            bookEntity: BookEntity,
        ) {
            dao.saveDiscussionWithBook(discussionEntity, bookEntity)
        }

        override suspend fun getDiscussion(id: Long): DiscussionWithBook? = dao.getDiscussionWithBook(id)

        override suspend fun hasDiscussion(): Boolean = dao.hasDiscussion()

        override suspend fun getBook(id: Long): BookEntity = dao.getBook(id)

        override suspend fun deleteDiscussion() = dao.deleteDiscussion()

        override suspend fun getDiscussionCount(): Int = dao.getDraftDiscussionCount()

        override suspend fun getDiscussions(): List<DiscussionWithBook> = dao.getDiscussions()
    }
