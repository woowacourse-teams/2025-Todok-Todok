package com.team.domain.repository

import com.team.domain.model.Discussion

interface DiscussionRepository {
    suspend fun getDiscussion(id: Long): Result<Discussion>

    suspend fun getDiscussions(): List<Discussion>

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
