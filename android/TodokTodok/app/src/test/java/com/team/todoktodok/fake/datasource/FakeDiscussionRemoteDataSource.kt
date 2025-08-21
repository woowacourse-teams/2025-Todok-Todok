package com.team.todoktodok.fake.datasource

import com.team.domain.model.exception.NetworkResult
import com.team.todoktodok.data.datasource.discussion.DiscussionRemoteDataSource
import com.team.todoktodok.data.network.model.LikeAction
import com.team.todoktodok.data.network.response.discussion.DiscussionResponse
import com.team.todoktodok.data.network.response.latest.LatestDiscussionsResponse
import com.team.todoktodok.data.network.response.latest.PageInfoResponse
import com.team.todoktodok.fixture.DISCUSSION_RESPONSE
import com.team.todoktodok.fixture.LATEST_DISCUSSIONS_RESPONSE
import retrofit2.Response

class FakeDiscussionRemoteDataSource : DiscussionRemoteDataSource {

    override suspend fun getLatestDiscussions(
        size: Int,
        cursor: String?,
    ): NetworkResult<LatestDiscussionsResponse> {
        val startIndex = cursor?.toIntOrNull() ?: 0
        val endIndex = (startIndex + size).coerceAtMost(LATEST_DISCUSSIONS_RESPONSE.size)
        val pageDiscussions = LATEST_DISCUSSIONS_RESPONSE.subList(startIndex, endIndex)

        val hasNext = endIndex < LATEST_DISCUSSIONS_RESPONSE.size
        val nextCursor = if (hasNext) endIndex.toString() else ""

        val pageInfo = PageInfoResponse(hasNext, nextCursor)
        val page = LatestDiscussionsResponse(pageDiscussions, pageInfo)

        return NetworkResult.Success(page)
    }

    override suspend fun getDiscussion(id: Long): NetworkResult<DiscussionResponse> =
        NetworkResult.Success(
            DISCUSSION_RESPONSE.find { id == it.discussionId }
                ?: throw IllegalArgumentException(),
        )

    override suspend fun saveDiscussionRoom(
        bookId: Long,
        discussionTitle: String,
        discussionOpinion: String,
    ): Response<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun editDiscussionRoom(
        discussionId: Long,
        discussionTitle: String,
        discussionOpinion: String,
    ): NetworkResult<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteDiscussion(discussionId: Long): NetworkResult<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun toggleLike(discussionId: Long): NetworkResult<LikeAction> {
        TODO("Not yet implemented")
    }

    override suspend fun reportDiscussion(discussionId: Long): NetworkResult<Unit> {
        TODO("Not yet implemented")
    }
}
