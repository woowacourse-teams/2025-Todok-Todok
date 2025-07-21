package com.example.todoktodok.data.repository

import com.example.todoktodok.DISCUSSION_ROOMS
import com.example.todoktodok.data.datasource.DefaultDiscussionRoomDataSource
import com.example.todoktodok.data.datasource.DiscussionRoomDataSource
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DefaultDiscussionRoomRepositoryTest {
    private lateinit var discussionRoomDataSource: DiscussionRoomDataSource
    private lateinit var defaultDiscussionRoomRepository: DefaultDiscussionRoomRepository

    @BeforeEach
    fun setUp() {
        discussionRoomDataSource = DefaultDiscussionRoomDataSource()
        defaultDiscussionRoomRepository = DefaultDiscussionRoomRepository(discussionRoomDataSource)
    }

    @Test
    fun `모든 토론방을 출력한다`() {
        // given
        val expected = DISCUSSION_ROOMS
        // then
        assertThat(defaultDiscussionRoomRepository.getDiscussionRooms()).isEqualTo(expected)
    }
}
