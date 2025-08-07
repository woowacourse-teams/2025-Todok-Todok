package com.team.todoktodok.fake

import com.team.domain.model.Discussion
import com.team.domain.model.DiscussionFilter
import com.team.domain.model.member.DiscussionRoom
import com.team.domain.repository.DiscussionRepository
import com.team.todoktodok.fixture.DISCUSSIONS

class FakeDiscussionRepository : DiscussionRepository {
    private val discussions = DISCUSSIONS

    override suspend fun getDiscussion(id: Long): Result<Discussion> =
        runCatching {
            discussions.find { id == it.id } ?: throw IllegalArgumentException()
        }

    override suspend fun getDiscussions(
        type: DiscussionFilter,
        keyword: String?,
    ): List<Discussion> = discussions

    override suspend fun saveDiscussionRoom(
        bookId: Long,
        discussionTitle: String,
        discussionOpinion: String,
    ): Long {
        TODO("Not yet implemented")
    }

    override suspend fun editDiscussionRoom(
        discussionId: Long,
        discussionRoom: DiscussionRoom,
    ) {
        TODO("Not yet implemented")
    }
}
