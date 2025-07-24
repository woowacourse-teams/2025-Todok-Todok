package com.example.domain.repository

import com.example.domain.model.Discussion

interface DiscussionRepository {
    fun getDiscussion(id: Long): Result<Discussion>

    fun getDiscussions(): List<Discussion>
}
