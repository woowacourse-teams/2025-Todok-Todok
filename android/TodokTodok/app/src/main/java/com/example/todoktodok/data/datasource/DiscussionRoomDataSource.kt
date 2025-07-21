package com.example.todoktodok.data.datasource

import com.example.domain.model.DiscussionRoom

interface DiscussionRoomDataSource {
    fun getDiscussionRooms(): List<DiscussionRoom>
}
