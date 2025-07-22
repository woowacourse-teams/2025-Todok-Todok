package com.example.todoktodok.data.datasource

import com.example.domain.model.DiscussionRoom

interface DiscussionRoomDataSource {
    fun getDiscussionRoom(id: Long): Result<DiscussionRoom>

    fun getDiscussionRooms(): List<DiscussionRoom>
}
