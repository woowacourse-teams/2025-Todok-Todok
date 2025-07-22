package com.example.domain.repository

import com.example.domain.model.DiscussionRoom

interface DiscussionRoomRepository {
    fun getDiscussionRoom(id: Long): Result<DiscussionRoom>

    fun getDiscussionRooms(): List<DiscussionRoom>
}
