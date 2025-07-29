package com.team.todoktodok.data.datasource.discussion

import com.team.todoktodok.data.network.request.DiscussionRequest
import com.team.todoktodok.data.network.request.DiscussionRoomRequest
import com.team.todoktodok.data.network.response.discussion.DiscussionResponse
import com.team.todoktodok.data.network.service.DiscussionService
import retrofit2.Response

class DefaultDiscussionRemoteDataSource(
    private val discussionService: DiscussionService,
) : DiscussionRemoteDataSource {
    override suspend fun getDiscussion(id: Long): Result<DiscussionResponse> =
        runCatching {
            discussionService.fetchDiscussion(id)
        }

    override suspend fun getDiscussions(): List<DiscussionResponse> = discussionService.fetchDiscussions()

    override suspend fun saveDiscussion(discussionRequest: DiscussionRequest): Long =
        discussionService.saveDiscussion(discussionRequest).extractCartItemId()

    override suspend fun saveDiscussionRoom(discussionRequest: DiscussionRoomRequest) {
        discussionService.saveDiscussionRoom(discussionRequest)
    }

    private fun Response<*>.extractCartItemId(): Long {
        val locationHeader = this.headers()[HEADER_LOCATION]
        return locationHeader
            ?.substringAfter(HEADER_CART_ID_PREFIX)
            ?.takeWhile { it.isDigit() }
            ?.toLongOrNull()
            ?: throw IllegalStateException()
    }

    companion object {
        private const val HEADER_LOCATION = "Location"
        private const val HEADER_CART_ID_PREFIX = "/discussions/"
    }
}
