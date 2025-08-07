package com.team.todoktodok.data.datasource.discussion

import com.team.domain.model.DiscussionFilter
import com.team.todoktodok.data.network.response.discussion.DiscussionResponse
import retrofit2.Response

interface DiscussionRemoteDataSource {
    suspend fun getDiscussion(id: Long): Result<DiscussionResponse>

    suspend fun getDiscussions(
        type: DiscussionFilter,
        keyword: String? = null,
    ): List<DiscussionResponse>

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
}
