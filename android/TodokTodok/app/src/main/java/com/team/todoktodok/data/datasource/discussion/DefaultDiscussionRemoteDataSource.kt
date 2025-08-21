package com.team.todoktodok.data.datasource.discussion

import com.team.domain.model.DiscussionFilter
import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.exception.toDomain
import com.team.todoktodok.data.core.ext.mapToggleLikeResponse
import com.team.todoktodok.data.network.model.LikeAction
import com.team.todoktodok.data.network.request.DiscussionRoomRequest
import com.team.todoktodok.data.network.request.EditDiscussionRoomRequest
import com.team.todoktodok.data.network.response.discussion.DiscussionResponse
import com.team.todoktodok.data.network.response.latest.LatestDiscussionsResponse
import com.team.todoktodok.data.network.service.DiscussionService
import retrofit2.Response

class DefaultDiscussionRemoteDataSource(
    private val discussionService: DiscussionService,
) : DiscussionRemoteDataSource {
    override suspend fun getLatestDiscussions(
        size: Int,
        cursor: String?,
    ): NetworkResult<LatestDiscussionsResponse> = discussionService.fetchLatestDiscussions(size, cursor)

    override suspend fun getDiscussion(id: Long): NetworkResult<DiscussionResponse> = discussionService.fetchDiscussion(id)

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
    ): NetworkResult<Unit> =
        discussionService.editDiscussionRoom(
            discussionId = discussionId,
            editDiscussionRoomRequest =
                EditDiscussionRoomRequest(
                    discussionTitle,
                    discussionOpinion,
                ),
        )

    override suspend fun deleteDiscussion(discussionId: Long): NetworkResult<Unit> = discussionService.deleteDiscussion(discussionId)

    override suspend fun toggleLike(discussionId: Long): NetworkResult<LikeAction> =
        runCatching {
            discussionService.toggleLike(discussionId).mapToggleLikeResponse()
        }.getOrElse {
            NetworkResult.Failure(it.toDomain())
        }

    override suspend fun reportDiscussion(discussionId: Long): NetworkResult<Unit> = discussionService.reportDiscussion(discussionId)
}
