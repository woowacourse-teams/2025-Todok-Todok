package com.team.domain.repository

import com.team.domain.model.Discussion
import com.team.domain.model.DiscussionFilter

interface DiscussionRepository {
    suspend fun getDiscussion(id: Long): Result<Discussion>

    suspend fun getDiscussions(
        type: DiscussionFilter,
        keyword: String? = null,
    ): List<Discussion>
}
