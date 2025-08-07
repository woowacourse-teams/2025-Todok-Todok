package com.team.todoktodok.data.repository

import com.team.domain.model.Discussion
import com.team.domain.model.DiscussionFilter
import com.team.domain.model.member.DiscussionRoom
import com.team.domain.repository.DiscussionRepository
import com.team.todoktodok.data.datasource.discussion.DiscussionRemoteDataSource
import com.team.todoktodok.data.network.response.discussion.toDomain

class DefaultDiscussionRepository(
    private val discussionRemoteDataSource: DiscussionRemoteDataSource,
) : DiscussionRepository {
    override suspend fun getDiscussion(id: Long): Result<Discussion> = discussionRemoteDataSource.getDiscussion(id).map { it.toDomain() }

    override suspend fun getDiscussions(
        type: DiscussionFilter,
        keyword: String?,
    ): List<Discussion> = discussionRemoteDataSource.getDiscussions(type, keyword).map { it.toDomain() }

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
    ) {
        discussionRemoteDataSource.editDiscussionRoom(
            discussionId = discussionId,
            discussionTitle = discussionRoom.title,
            discussionOpinion = discussionRoom.opinion,
        )
    }

    companion object {
        private const val HEADER_LOCATION: String = "location"
        private const val HEADER_DISCUSSION_ID_PREFIX: String = "/"
        private const val ERROR_SEVER_RESPONSE: String = "서버의 응답이 잘못되었습니다"
        private const val ERROR_NETWORK_WITH_SEVER: String = "서버와 통신에 오류가 있었습니다"
    }
}
