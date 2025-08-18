package com.team.domain.repository

import com.team.domain.model.Discussion
import com.team.domain.model.DiscussionFilter
import com.team.domain.model.LikeStatus
import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.member.DiscussionRoom

interface DiscussionRepository {
    suspend fun getDiscussion(id: Long): Result<Discussion>

    suspend fun getDiscussions(
        type: DiscussionFilter,
        keyword: String? = null,
    ): NetworkResult<List<Discussion>>

    suspend fun saveDiscussionRoom(
        bookId: Long,
        discussionTitle: String,
        discussionOpinion: String,
    ): Long

    suspend fun editDiscussionRoom(
        discussionId: Long,
        discussionRoom: DiscussionRoom,
    )

    suspend fun deleteDiscussion(discussionId: Long)

    suspend fun toggleLike(discussionId: Long): LikeStatus

    suspend fun reportDiscussion(discussionId: Long)
}
