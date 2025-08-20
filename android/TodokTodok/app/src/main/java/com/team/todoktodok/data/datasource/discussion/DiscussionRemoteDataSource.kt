package com.team.todoktodok.data.datasource.discussion

import com.team.domain.model.DiscussionFilter
import com.team.domain.model.exception.NetworkResult
import com.team.todoktodok.data.network.model.LikeAction
import com.team.todoktodok.data.network.response.discussion.DiscussionResponse
import com.team.todoktodok.data.network.response.latest.LatestDiscussionsResponse
import retrofit2.Response

interface DiscussionRemoteDataSource {

    suspend fun getLatestDiscussions(
        size: Int,
        cursor: String? = null,
    ): NetworkResult<LatestDiscussionsResponse>

    suspend fun getDiscussion(id: Long): NetworkResult<DiscussionResponse>

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
    ): NetworkResult<Unit>

    suspend fun deleteDiscussion(discussionId: Long): NetworkResult<Unit>

    suspend fun toggleLike(discussionId: Long): NetworkResult<LikeAction>

    suspend fun reportDiscussion(discussionId: Long): NetworkResult<Unit>
}
