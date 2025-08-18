package com.team.todoktodok.data.datasource.discussion

import com.team.domain.model.DiscussionFilter
import com.team.domain.model.exception.NetworkResult
import com.team.todoktodok.data.network.model.LikeAction
import com.team.todoktodok.data.network.request.DiscussionRoomRequest
import com.team.todoktodok.data.network.request.EditDiscussionRoomRequest
import com.team.todoktodok.data.network.response.discussion.DiscussionResponse
import com.team.todoktodok.data.network.service.DiscussionService
import retrofit2.Response

class DefaultDiscussionRemoteDataSource(
    private val discussionService: DiscussionService,
) : DiscussionRemoteDataSource {
    override suspend fun getDiscussion(id: Long): Result<DiscussionResponse> = runCatching { discussionService.fetchDiscussion(id) }

    override suspend fun getDiscussions(
        type: DiscussionFilter,
        keyword: String?,
    ): NetworkResult<List<DiscussionResponse>> = discussionService.fetchDiscussions(keyword, type.name)

    override suspend fun saveDiscussionRoom(
        bookId: Long,
        discussionTitle: String,
        discussionOpinion: String,
    ): Response<Unit> {
        val discussionRoomRequest =
            DiscussionRoomRequest(
                bookId,
                discussionTitle,
                discussionOpinion,
            )
        return discussionService.saveDiscussionRoom(discussionRoomRequest)
    }

    override suspend fun editDiscussionRoom(
        discussionId: Long,
        discussionTitle: String,
        discussionOpinion: String,
    ): Response<Unit> =
        discussionService.editDiscussionRoom(
            discussionId = discussionId,
            editDiscussionRoomRequest =
                EditDiscussionRoomRequest(
                    discussionTitle,
                    discussionOpinion,
                ),
        )

    override suspend fun deleteDiscussion(discussionId: Long) {
        discussionService.deleteDiscussion(discussionId)
    }

    override suspend fun toggleLike(discussionId: Long): LikeAction =
        when (discussionService.toggleLike(discussionId).code()) {
            201 -> LikeAction.LIKE
            204 -> LikeAction.UNLIKE
            else -> throw IllegalStateException()
        }

    override suspend fun reportDiscussion(discussionId: Long) {
        discussionService.reportDiscussion(discussionId)
    }

    private fun Response<*>.extractDiscussionId(): Long {
        val locationHeader = headers()[HEADER_LOCATION]
        return locationHeader
            ?.substringAfter(HEADER_DISCUSSION_ID_PREFIX)
            ?.takeWhile { it.isDigit() }
            ?.toLongOrNull()
            ?: throw IllegalStateException("Invalid or missing Location header: $locationHeader")
    }

    companion object {
        private const val HEADER_LOCATION = "Location"
        private const val HEADER_DISCUSSION_ID_PREFIX = "/discussions/"
    }
}
