package com.team.domain.repository

import com.team.domain.model.Discussion
import com.team.domain.model.DiscussionFilter

interface DiscussionRepository {
    suspend fun getDiscussion(id: Long): Result<Discussion>

    suspend fun getDiscussions(
        type: DiscussionFilter,
        keyword: String? = null,
    ): List<Discussion>

    suspend fun saveDiscussion(
        noteId: Long,
        discussionTitle: String,
        discussionOpinion: String,
    ): Long

    suspend fun saveDiscussionRoom(
        noteId: Long,
        discussionTitle: String,
        discussionOpinion: String,
    )
}
