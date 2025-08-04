package com.team.todoktodok.data.datasource.discussion

import com.team.domain.model.DiscussionFilter
import com.team.todoktodok.data.network.response.discussion.DiscussionResponse

interface DiscussionRemoteDataSource {
    suspend fun getDiscussion(id: Long): Result<DiscussionResponse>

    suspend fun getDiscussions(
        type: DiscussionFilter,
        keyword: String? = null,
    ): List<DiscussionResponse>
}
