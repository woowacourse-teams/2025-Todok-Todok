package com.example.todoktodok.data.datasource.discussion

import com.example.todoktodok.data.network.response.discussion.DiscussionResponse
import com.example.todoktodok.data.network.service.AUTH_KEY
import com.example.todoktodok.data.network.service.DiscussionService

class DefaultDiscussionRemoteDataSource(
    private val discussionService: DiscussionService,
) : DiscussionRemoteDataSource {
    override suspend fun getDiscussion(id: Long): Result<DiscussionResponse> =
        runCatching {
            discussionService.fetchDiscussion(
                AUTH_KEY,
                id,
            )
        }

    override suspend fun getDiscussions(): List<DiscussionResponse> =
        discussionService.fetchDiscussions(
            AUTH_KEY,
        )
}
