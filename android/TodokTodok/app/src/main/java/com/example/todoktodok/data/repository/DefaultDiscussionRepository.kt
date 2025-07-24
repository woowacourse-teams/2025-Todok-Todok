package com.example.todoktodok.data.repository

import com.example.domain.model.Discussion
import com.example.domain.repository.DiscussionRepository
import com.example.todoktodok.data.datasource.discussion.DiscussionRemoteDataSource
import com.example.todoktodok.data.network.response.discussion.toDomain

class DefaultDiscussionRepository(
    private val discussionRemoteDataSource: DiscussionRemoteDataSource,
) : DiscussionRepository {
    override suspend fun getDiscussion(id: Long): Result<Discussion> = discussionRemoteDataSource.getDiscussion(id).map { it.toDomain() }

    override suspend fun getDiscussions() = discussionRemoteDataSource.getDiscussions().map { it.toDomain() }
}
