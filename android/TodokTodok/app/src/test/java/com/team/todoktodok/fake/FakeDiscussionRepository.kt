package com.team.todoktodok.fake

import com.team.domain.model.Discussion
import com.team.domain.model.DiscussionFilter
import com.team.domain.model.LikeStatus
import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.exception.map
import com.team.domain.model.latest.LatestDiscussionPage
import com.team.domain.model.member.DiscussionRoom
import com.team.domain.repository.DiscussionRepository
import com.team.todoktodok.fake.datasource.FakeDiscussionRemoteDataSource
import com.team.todoktodok.fixture.DISCUSSIONS

class FakeDiscussionRepository : DiscussionRepository {
    private val dataSource = FakeDiscussionRemoteDataSource()
    private val discussions = DISCUSSIONS
    override suspend fun getLatestDiscussions(size: Int, cursor: String?): NetworkResult<LatestDiscussionPage> {
        return dataSource.getLatestDiscussions(size, cursor).map { it.toDomain() }
    }

    override suspend fun getDiscussion(id: Long): NetworkResult<Discussion> =
        NetworkResult.Success(discussions.find { id == it.id } ?: throw IllegalArgumentException())

    override suspend fun getDiscussions(
        type: DiscussionFilter,
        keyword: String?,
    ): NetworkResult<List<Discussion>> = NetworkResult.Success(discussions)

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
    ): NetworkResult<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteDiscussion(discussionId: Long): NetworkResult<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun toggleLike(discussionId: Long): NetworkResult<LikeStatus> {
        TODO("Not yet implemented")
    }

    override suspend fun reportDiscussion(discussionId: Long): NetworkResult<Unit> {
        TODO("Not yet implemented")
    }
}
