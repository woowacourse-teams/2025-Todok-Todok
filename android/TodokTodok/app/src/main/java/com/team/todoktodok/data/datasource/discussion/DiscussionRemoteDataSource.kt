package com.team.todoktodok.data.datasource.discussion

import com.team.domain.model.DiscussionFilter
import com.team.domain.model.exception.NetworkResult
import com.team.todoktodok.data.network.model.LikeAction
import com.team.todoktodok.data.network.response.discussion.DiscussionResponse
import retrofit2.Response

interface DiscussionRemoteDataSource {
    suspend fun getDiscussion(id: Long): Result<DiscussionResponse>

    suspend fun getDiscussions(
        type: DiscussionFilter,
        keyword: String? = null,
    ): NetworkResult<List<DiscussionResponse>>

    suspend fun saveDiscussionRoom(
        bookId: Long,
        discussionTitle: String,
        discussionOpinion: String,
    ): Response<Unit>

    suspend fun editDiscussionRoom(
        discussionId: Long,
        discussionTitle: String,
        discussionOpinion: String,
    ): Response<Unit>

    suspend fun deleteDiscussion(discussionId: Long)

    suspend fun toggleLike(discussionId: Long): LikeAction

    suspend fun reportDiscussion(discussionId: Long)
}
