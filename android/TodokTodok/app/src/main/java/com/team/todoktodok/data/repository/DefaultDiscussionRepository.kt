package com.team.todoktodok.data.repository

import com.team.domain.model.Book
import com.team.domain.model.Discussion
import com.team.domain.model.LikeStatus
import com.team.domain.model.active.ActivatedDiscussionPage
import com.team.domain.model.discussionroom.DiscussionRoom
import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.exception.map
import com.team.domain.model.latest.LatestDiscussionPage
import com.team.domain.repository.DiscussionRepository
import com.team.todoktodok.data.datasource.discussion.DiscussionLocalDataSource
import com.team.todoktodok.data.datasource.discussion.DiscussionRemoteDataSource
import com.team.todoktodok.data.local.discussion.DiscussionRoomEntity
import com.team.todoktodok.data.local.discussion.toDomain
import com.team.todoktodok.data.local.discussion.toEntity
import com.team.todoktodok.data.network.model.toStatus
import com.team.todoktodok.data.network.response.discussion.toDomain

class DefaultDiscussionRepository(
    private val discussionRemoteDataSource: DiscussionRemoteDataSource,
    private val discussionLocalDataSource: DiscussionLocalDataSource,
) : DiscussionRepository {
    override suspend fun getSearchDiscussion(keyword: String): NetworkResult<List<Discussion>> =
        discussionRemoteDataSource.getSearchDiscussion(keyword).map { discussions ->
            discussions.map { it.toDomain() }
        }

    override suspend fun getActivatedDiscussion(
        period: Int,
        size: Int,
        cursor: String?,
    ): NetworkResult<ActivatedDiscussionPage> =
        discussionRemoteDataSource
            .getActivatedDiscussion(period, size, cursor)
            .map { it.toDomain() }

    override suspend fun getHotDiscussion(
        period: Int,
        count: Int,
    ): NetworkResult<List<Discussion>> =
        discussionRemoteDataSource
            .getHotDiscussion(period, count)
            .map { discussions -> discussions.map { it.toDomain() } }

    override suspend fun getLatestDiscussions(
        size: Int,
        cursor: String?,
    ): NetworkResult<LatestDiscussionPage> =
        discussionRemoteDataSource
            .getLatestDiscussions(size, cursor)
            .map { discussions -> discussions.toDomain() }

    override suspend fun getDiscussion(id: Long): NetworkResult<Discussion> =
        discussionRemoteDataSource.fetchDiscussion(id).map { it.toDomain() }

    override suspend fun getDraftDiscussion(id: Long): DiscussionRoom? =
        discussionLocalDataSource.getDiscussion(id)?.discussionRoomEntity?.toDomain()

    override suspend fun getDiscussions(): List<DiscussionRoom> {
        val result = discussionLocalDataSource.getDiscussions()
        return result.map { it.discussionRoomEntity.toDomain() }
    }

    override suspend fun getDraftDiscussionCount(): Int =
        discussionLocalDataSource.getDiscussionCount()

    override suspend fun saveDiscussionRoom(
        bookId: Long,
        discussionTitle: String,
        discussionOpinion: String,
    ): Long {
        val response =
            discussionRemoteDataSource.saveDiscussionRoom(
                bookId = bookId,
                discussionTitle = discussionTitle,
                discussionOpinion = discussionOpinion,
            )
        if (response.isSuccessful) {
            val location = response.headers()[HEADER_LOCATION]
            val id = location?.substringAfterLast(HEADER_DISCUSSION_ID_PREFIX)
            return id?.toLongOrNull() ?: throw IllegalStateException(ERROR_SEVER_RESPONSE)
        } else {
            throw IllegalStateException(ERROR_NETWORK_WITH_SEVER)
        }
    }

    override suspend fun editDiscussionRoom(
        discussionId: Long,
        discussionRoom: DiscussionRoom,
    ): NetworkResult<Unit> =
        discussionRemoteDataSource.editDiscussionRoom(
            discussionId = discussionId,
            discussionTitle = discussionRoom.title,
            discussionOpinion = discussionRoom.opinion,
        )

    override suspend fun deleteDiscussion(discussionId: Long): NetworkResult<Unit> =
        discussionRemoteDataSource.deleteDiscussion(discussionId)

    override suspend fun toggleLike(discussionId: Long): NetworkResult<LikeStatus> =
        discussionRemoteDataSource.toggleLike(discussionId).map { it.toStatus() }

    override suspend fun reportDiscussion(
        discussionId: Long,
        reason: String,
    ): NetworkResult<Unit> = discussionRemoteDataSource.reportDiscussion(discussionId, reason)

    override suspend fun deleteDiscussionRoom() = discussionLocalDataSource.deleteDiscussion()

    override suspend fun hasDiscussion(): Boolean = discussionLocalDataSource.hasDiscussion()

    override suspend fun getBook(id: Long): Book = discussionLocalDataSource.getBook(id).toDomain()

    override suspend fun saveDiscussionRoom(
        book: Book,
        discussionTitle: String,
        discussionOpinion: String,
    ) {
        discussionLocalDataSource.saveDiscussion(
            discussionEntity =
                DiscussionRoomEntity(
                    title = discussionTitle,
                    opinion = discussionOpinion,
                    bookId = book.id,
                ),
            bookEntity = book.toEntity(),
        )
    }

    companion object {
        private const val HEADER_LOCATION: String = "location"
        private const val HEADER_DISCUSSION_ID_PREFIX: String = "/"
        private const val ERROR_SEVER_RESPONSE: String = "서버의 응답이 잘못되었습니다"
        private const val ERROR_NETWORK_WITH_SEVER: String = "서버와 통신에 오류가 있었습니다"
    }
}
