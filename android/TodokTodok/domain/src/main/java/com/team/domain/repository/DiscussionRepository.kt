package com.team.domain.repository

import com.team.domain.model.Book
import com.team.domain.model.Discussion
import com.team.domain.model.DiscussionPage
import com.team.domain.model.LikeStatus
import com.team.domain.model.discussionroom.DiscussionRoom
import com.team.domain.model.exception.NetworkResult

interface DiscussionRepository {
    suspend fun getSearchDiscussion(keyword: String): NetworkResult<List<Discussion>>

    suspend fun getActivatedDiscussion(
        period: Int = DEFAULT_HOT_DISCUSSION_PERIOD,
        size: Int = PAGING_SIZE,
        cursor: String? = null,
    ): NetworkResult<DiscussionPage>

    suspend fun getLikedDiscussion(): NetworkResult<List<Discussion>>

    suspend fun getHotDiscussion(
        period: Int = DEFAULT_HOT_DISCUSSION_PERIOD,
        count: Int = DEFAULT_HOT_DISCUSSION_COUNT,
    ): NetworkResult<List<Discussion>>

    suspend fun getLatestDiscussions(
        size: Int = PAGING_SIZE,
        cursor: String? = null,
    ): NetworkResult<DiscussionPage>

    suspend fun getDiscussion(id: Long): NetworkResult<Discussion>

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

    suspend fun saveDiscussionRoom(
        book: Book,
        discussionTitle: String,
        discussionOpinion: String,
    )

    suspend fun hasDiscussion(): Boolean

    suspend fun getBook(): Book

    suspend fun getDiscussion(): DiscussionRoom?

    suspend fun reportDiscussion(
        discussionId: Long,
        reason: String,
    ): NetworkResult<Unit>

    suspend fun deleteDiscussionRoom()

    companion object {
        private const val PAGING_SIZE = 15
        private const val DEFAULT_HOT_DISCUSSION_PERIOD = 7
        private const val DEFAULT_HOT_DISCUSSION_COUNT = 5
    }
}
