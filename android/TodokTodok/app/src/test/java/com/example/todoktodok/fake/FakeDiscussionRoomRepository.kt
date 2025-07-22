package com.example.todoktodok.fake

import com.example.domain.model.DiscussionRoom
import com.example.domain.repository.DiscussionRoomRepository
import com.example.todoktodok.fixture.DISCUSSION_ROOMS

class FakeDiscussionRoomRepository : DiscussionRoomRepository {
    private val discussionRooms = DISCUSSION_ROOMS

    override fun getDiscussionRoom(id: Long): Result<DiscussionRoom> =
        runCatching {
            DISCUSSION_ROOMS.find { id == it.id } ?: throw IllegalArgumentException()
        }

    override fun getDiscussionRooms(): List<DiscussionRoom> = discussionRooms
}
