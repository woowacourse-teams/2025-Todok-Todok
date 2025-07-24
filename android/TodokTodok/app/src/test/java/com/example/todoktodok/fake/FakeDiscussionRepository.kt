package com.example.todoktodok.fake

import com.example.domain.model.Discussion
import com.example.domain.repository.DiscussionRepository
import com.example.todoktodok.fixture.DISCUSSION

class FakeDiscussionRepository : DiscussionRepository {
    private val discussionRooms = DISCUSSION

    override fun getDiscussion(id: Long): Result<Discussion> =
        runCatching {
            DISCUSSION.find { id == it.id } ?: throw IllegalArgumentException()
        }

    override fun getDiscussions(): List<Discussion> = discussionRooms
}
