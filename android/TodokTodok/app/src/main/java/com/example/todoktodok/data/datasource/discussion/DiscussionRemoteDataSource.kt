package com.example.todoktodok.data.datasource.discussion

import com.example.todoktodok.data.network.response.discussion.DiscussionResponse

interface DiscussionRemoteDataSource {
    suspend fun getDiscussion(id: Long): Result<DiscussionResponse>

    suspend fun getDiscussions(): List<DiscussionResponse>
}
