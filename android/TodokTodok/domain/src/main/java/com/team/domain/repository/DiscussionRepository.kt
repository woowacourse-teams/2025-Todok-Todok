package com.team.domain.repository

import com.team.domain.model.Discussion
import com.team.domain.model.DiscussionFilter
import com.team.domain.model.LikeStatus
import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.latest.LatestDiscussionPage
import com.team.domain.model.member.DiscussionRoom

interface DiscussionRepository {
    suspend fun getLatestDiscussions(
        size: Int,
        cursor: String? = null,
    ): NetworkResult<LatestDiscussionPage>

    suspend fun getDiscussion(id: Long): NetworkResult<Discussion>

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
    ): NetworkResult<Unit>

    suspend fun deleteDiscussion(discussionId: Long): NetworkResult<Unit>

    suspend fun toggleLike(discussionId: Long): NetworkResult<LikeStatus>

    suspend fun reportDiscussion(discussionId: Long): NetworkResult<Unit>
}
