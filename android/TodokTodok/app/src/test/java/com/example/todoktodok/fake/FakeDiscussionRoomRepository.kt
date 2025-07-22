package com.example.todoktodok.fake

import com.example.domain.model.DiscussionRoom
import com.example.domain.repository.DiscussionRoomRepository
import com.example.todoktodok.fixture.DISCUSSION_ROOMS

class FakeDiscussionRoomRepository : DiscussionRoomRepository {
    private val discussionRooms = DISCUSSION_ROOMS

    override fun getDiscussionRoom(id: Long): DiscussionRoom = discussionRooms.find { it.id == id }!!

    override fun getDiscussionRooms(): List<DiscussionRoom> = discussionRooms
}
