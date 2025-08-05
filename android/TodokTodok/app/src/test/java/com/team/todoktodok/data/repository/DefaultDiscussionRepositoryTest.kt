package com.team.todoktodok.data.repository

import com.team.domain.model.Book
import com.team.domain.model.Discussion
import com.team.domain.model.DiscussionFilter
import com.team.domain.model.member.Nickname
import com.team.domain.model.member.User
import com.team.todoktodok.data.datasource.discussion.DiscussionRemoteDataSource
import com.team.todoktodok.fake.datasource.FakeDiscussionRemoteDataSource
import com.team.todoktodok.fixture.DISCUSSIONS
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

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

            // when
            val result = defaultDiscussionRepository.getDiscussions(DiscussionFilter.ALL)

            // then
            assertEquals(result, expected)
        }

    @Test
    fun `MINE 필터는 memberId가 1인 나의 토론만 반환한다`() =
        runTest {
            // given
            val type = DiscussionFilter.MINE
            val expected =
                listOf(
                    Discussion(
                        id = 1,
                        discussionTitle = "JPA 성능 최적화",
                        book =
                            Book(
                                id = 1,
                                title = "자바 ORM 표준 JPA 프로그래밍",
                                author = "김영한",
                                image = "",
                            ),
                        writer = User(id = 1, nickname = Nickname(value = "홍길동")),
                        createAt = LocalDateTime.of(2025, 7, 12, 12, 0),
                        discussionOpinion = "fetch join을 남발하면 안됩니다.",
                    ),
                )

            // when
            val result = defaultDiscussionRepository.getDiscussions(type)

            // then
            assertEquals(result, expected)
        }

    @Test
    fun `ALL 필터는 모든 토론을 반환한다`() =
        runTest {
            // given
            val type = DiscussionFilter.ALL
            val expected = DISCUSSIONS
            // when
            val result = defaultDiscussionRepository.getDiscussions(type)

            // then
            assertEquals(result, expected)
        }

    @Test
    fun `키워드와 MINE 필터를 함께 사용하면 해당 조건을 모두 만족하는 토론만 반환한다`() =
        runTest {
            // given
            val keyword = "JPA"
            val type = DiscussionFilter.MINE
            val expected =
                listOf(
                    Discussion(
                        id = 1L,
                        discussionTitle = "JPA 성능 최적화",
                        book = Book(1L, "자바 ORM 표준 JPA 프로그래밍", "김영한", ""),
                        writer = User(1L, Nickname("홍길동")),
                        createAt = LocalDateTime.of(2025, 7, 12, 12, 0),
                        discussionOpinion = "fetch join을 남발하면 안됩니다.",
                    ),
                )

            // when
            val result = defaultDiscussionRepository.getDiscussions(type, keyword)

            // then
            assertEquals(result, expected)
        }
}
