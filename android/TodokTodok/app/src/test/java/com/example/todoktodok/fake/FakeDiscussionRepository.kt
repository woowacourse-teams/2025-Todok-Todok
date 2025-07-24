package com.example.todoktodok.fake

import com.example.domain.model.Discussion
import com.example.domain.repository.DiscussionRepository
import com.example.todoktodok.fixture.DISCUSSIONS

class FakeDiscussionRepository : DiscussionRepository {
    private val discussions = DISCUSSIONS

    override suspend fun getDiscussion(id: Long): Result<Discussion> =
        runCatching {
            discussions.find { id == it.id } ?: throw IllegalArgumentException()
        }

    override suspend fun getDiscussions(): List<Discussion> = discussions
}
