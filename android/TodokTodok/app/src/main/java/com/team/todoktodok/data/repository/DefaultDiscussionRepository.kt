package com.team.todoktodok.data.repository

import com.team.domain.model.Discussion
import com.team.domain.repository.DiscussionRepository
import com.team.todoktodok.data.datasource.discussion.DiscussionRemoteDataSource
import com.team.todoktodok.data.network.request.DiscussionRequest
import com.team.todoktodok.data.network.response.discussion.toDomain

class DefaultDiscussionRepository(
    private val discussionRemoteDataSource: DiscussionRemoteDataSource,
) : DiscussionRepository {
    override suspend fun getDiscussion(id: Long): Result<Discussion> = discussionRemoteDataSource.getDiscussion(id).map { it.toDomain() }

    override suspend fun getDiscussions() = discussionRemoteDataSource.getDiscussions().map { it.toDomain() }

    override suspend fun saveDiscussion(
        noteId: Long,
        discussionTitle: String,
        discussionOpinion: String,
    ): Long =
        discussionRemoteDataSource.saveDiscussion(
            DiscussionRequest(
                discussionTitle,
                discussionOpinion,
                noteId,
            ),
        )
}
