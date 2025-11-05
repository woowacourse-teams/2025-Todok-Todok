package com.team.data.datasource.discussion

import com.team.data.network.model.LikeAction
import com.team.data.network.response.discussion.DiscussionResponse
import com.team.data.network.response.discussion.page.ActivatedDiscussionPageResponse
import com.team.data.network.response.latest.LatestDiscussionsResponse
import com.team.domain.model.exception.NetworkResult
import retrofit2.Response

interface DiscussionRemoteDataSource {
    suspend fun getSearchDiscussion(keyword: String): NetworkResult<List<DiscussionResponse>>

    suspend fun getActivatedDiscussion(
        period: Int,
        size: Int,
        cursor: String? = null,
    ): NetworkResult<ActivatedDiscussionPageResponse>

    suspend fun getLikedDiscussion(): NetworkResult<List<DiscussionResponse>>

    suspend fun getHotDiscussion(
        period: Int,
        count: Int,
    ): NetworkResult<List<DiscussionResponse>>

    suspend fun getLatestDiscussions(
        size: Int,
        cursor: String? = null,
    ): NetworkResult<LatestDiscussionsResponse>

    suspend fun fetchDiscussion(id: Long): NetworkResult<DiscussionResponse>

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

    suspend fun reportDiscussion(
        discussionId: Long,
        reason: String,
    ): NetworkResult<Unit>
}
