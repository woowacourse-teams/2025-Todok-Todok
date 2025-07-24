package com.example.domain.repository

import com.example.domain.model.Discussion

interface DiscussionRepository {
    suspend fun getDiscussion(id: Long): Result<Discussion>

    suspend fun getDiscussions(): List<Discussion>
}
