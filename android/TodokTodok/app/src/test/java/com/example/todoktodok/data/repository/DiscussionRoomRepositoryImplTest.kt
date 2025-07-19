package com.example.todoktodok.data.repository

import com.example.todoktodok.DISCUSSION_ROOMS
import com.example.todoktodok.data.datasource.DiscussionRoomDataSource
import com.example.todoktodok.data.datasource.DiscussionRoomDataSourceImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DiscussionRoomRepositoryImplTest() {
    private lateinit var discussionRoomDataSource: DiscussionRoomDataSource
    private lateinit var discussionRoomRepositoryImpl: DiscussionRoomRepositoryImpl

    @BeforeEach
    fun setUp() {
        discussionRoomDataSource = DiscussionRoomDataSourceImpl()
        discussionRoomRepositoryImpl = DiscussionRoomRepositoryImpl(discussionRoomDataSource)
    }

    @Test
    fun `모든 토론방을 출력한다`() {
        //given
        val expected = DISCUSSION_ROOMS
        //then
        assertThat(discussionRoomRepositoryImpl.getDiscussionRooms()).isEqualTo(expected)
    }
}