package com.team.todoktodok.data.datasource.discussion

import com.team.todoktodok.data.network.request.DiscussionRequest
import com.team.todoktodok.data.network.request.DiscussionRoomRequest
import com.team.todoktodok.data.network.response.discussion.DiscussionResponse

interface DiscussionRemoteDataSource {
    suspend fun getDiscussion(id: Long): Result<DiscussionResponse>

    suspend fun getDiscussions(): List<DiscussionResponse>

    suspend fun saveDiscussion(discussionRequest: DiscussionRequest): Long

    suspend fun saveDiscussionRoom(discussionRequest: DiscussionRoomRequest)
}
