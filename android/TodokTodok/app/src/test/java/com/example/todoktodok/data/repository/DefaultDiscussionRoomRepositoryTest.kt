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
    fun `Id가 같은 토론방을 반환한다`() {
        // given
        val expected = DISCUSSION_ROOMS.find { it.id == 2L }
        //when
        val actual = defaultDiscussionRoomRepository.getDiscussionRoom(2)
        // then
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `모든 토론방을 반환한다`() {
        // given
        val expected = DISCUSSION_ROOMS
        // then
        assertThat(defaultDiscussionRoomRepository.getDiscussionRooms()).isEqualTo(expected)
    }
}
