package com.example.domain.repository

import com.example.domain.model.DiscussionRoom

interface DiscussionRoomRepository {
    fun getDiscussionRooms(): List<DiscussionRoom>
}
