package com.example.todoktodok.data.repository

import com.example.domain.model.Discussion
import com.example.domain.repository.DiscussionRepository
import com.example.todoktodok.data.datasource.discussion.DiscussionDataSource

class DefaultDiscussionRepository(
    private val discussionDataSource: DiscussionDataSource,
) : DiscussionRepository {
    override fun getDiscussion(id: Long): Result<Discussion> = discussionDataSource.getDiscussionRoom(id)

    override fun getDiscussions() = discussionDataSource.getDiscussionRooms()
}
