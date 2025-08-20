package com.team.todoktodok.data.datasource.discussion

import com.team.todoktodok.data.local.discussion.BookEntity
import com.team.todoktodok.data.local.discussion.DiscussionDao
import com.team.todoktodok.data.local.discussion.DiscussionRoomEntity
import com.team.todoktodok.data.local.discussion.DiscussionWithBook

class DefaultDiscussionLocalDataSource(
    private val dao: DiscussionDao,
) : DiscussionLocalDataSource {
    override suspend fun saveDiscussion(
        discussionEntity: DiscussionRoomEntity,
        bookEntity: BookEntity,
    ) {
        dao.saveDiscussionWithBook(discussionEntity, bookEntity)
    }

    override suspend fun getDiscussion(): DiscussionWithBook? = dao.getDiscussionWithBook()

    override suspend fun hasDiscussion(): Boolean = dao.hasDiscussion()

    override suspend fun getBook(): BookEntity = dao.getBook()
}
