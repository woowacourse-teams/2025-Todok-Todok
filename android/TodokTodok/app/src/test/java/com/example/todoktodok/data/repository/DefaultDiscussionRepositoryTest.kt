package com.example.todoktodok.data.repository

import com.example.todoktodok.data.datasource.discussion.DefaultDiscussionDataSource
import com.example.todoktodok.data.datasource.discussion.DiscussionDataSource
import com.example.todoktodok.fixture.DISCUSSION
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DefaultDiscussionRepositoryTest {
    private lateinit var discussionDataSource: DiscussionDataSource
    private lateinit var defaultDiscussionRepository: DefaultDiscussionRepository

    @BeforeEach
    fun setUp() {
        discussionDataSource = DefaultDiscussionDataSource()
        defaultDiscussionRepository = DefaultDiscussionRepository(discussionDataSource)
    }

    @Test
    fun `Id가 같은 토론방을 반환한다`() {
        // given
        val expected = DISCUSSION.find { it.id == 2L }
        // when
        val actual = defaultDiscussionRepository.getDiscussion(2).getOrNull()
        // then
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `모든 토론방을 반환한다`() {
        // given
        val expected = DISCUSSION
        // then
        assertThat(defaultDiscussionRepository.getDiscussions()).isEqualTo(expected)
    }
}
