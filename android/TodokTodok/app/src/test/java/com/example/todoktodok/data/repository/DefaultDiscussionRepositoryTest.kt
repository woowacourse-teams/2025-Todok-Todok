package com.example.todoktodok.data.repository

import com.example.todoktodok.data.datasource.discussion.DiscussionRemoteDataSource
import com.example.todoktodok.fake.datasource.FakeDiscussionRemoteDataSource
import com.example.todoktodok.fixture.DISCUSSIONS
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DefaultDiscussionRepositoryTest {
    private lateinit var discussionRemoteDataSource: DiscussionRemoteDataSource
    private lateinit var defaultDiscussionRepository: DefaultDiscussionRepository

    @BeforeEach
    fun setUp() {
        discussionRemoteDataSource = FakeDiscussionRemoteDataSource()
        defaultDiscussionRepository = DefaultDiscussionRepository(discussionRemoteDataSource)
    }

    @Test
    fun `Id가 같은 토론방을 반환한다`() =
        runTest {
            // given
            val expected = DISCUSSIONS.find { it.id == 2L }
            // when
            val actual = defaultDiscussionRepository.getDiscussion(2).getOrNull()
            // then
            assertThat(actual).isEqualTo(expected)
        }

    @Test
    fun `모든 토론방을 반환한다`() =
        runTest {
            // given
            val expected = DISCUSSIONS
            // then
            assertThat(defaultDiscussionRepository.getDiscussions()).isEqualTo(expected)
        }
}
