package com.example.todoktodok.data.repository

import com.example.domain.model.DiscussionRoom
import com.example.domain.repository.DiscussionRoomRepository
import com.example.todoktodok.data.datasource.DiscussionRoomDataSource

class DefaultDiscussionRoomRepository(
    private val discussionRoomDataSource: DiscussionRoomDataSource,
) : DiscussionRoomRepository {
    override fun getDiscussionRoom(id: Long): DiscussionRoom =
        discussionRoomDataSource.getDiscussionRoom(id)

    override fun getDiscussionRooms() = discussionRoomDataSource.getDiscussionRooms()
}
