package com.example.todoktodok.data.datasource.discussion

import com.example.domain.model.Discussion

interface DiscussionDataSource {
    fun getDiscussionRoom(id: Long): Result<Discussion>

    fun getDiscussionRooms(): List<Discussion>
}
