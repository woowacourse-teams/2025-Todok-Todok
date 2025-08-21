package com.team.todoktodok.data.repository

import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.exception.onSuccess
import com.team.todoktodok.data.datasource.discussion.DiscussionLocalDataSource
import com.team.todoktodok.data.datasource.discussion.DiscussionRemoteDataSource
import com.team.todoktodok.fake.datasource.FakeDiscussionRemoteDataSource
import com.team.todoktodok.fixture.DISCUSSIONS
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DefaultDiscussionRepositoryTest {
    private lateinit var discussionRemoteDataSource: DiscussionRemoteDataSource
    private lateinit var discussionLocalDataSource: DiscussionLocalDataSource
    private lateinit var defaultDiscussionRepository: DefaultDiscussionRepository

    @BeforeEach
    fun setUp() {
        discussionRemoteDataSource = FakeDiscussionRemoteDataSource()
        discussionLocalDataSource = mockk()
        defaultDiscussionRepository = DefaultDiscussionRepository(discussionRemoteDataSource, discussionLocalDataSource)
    }

    @Test
    fun `Id가 같은 토론방을 반환한다`() =
        runTest {
            // given
            val expected = DISCUSSIONS.find { it.id == 2L }
            // when
            val actual = defaultDiscussionRepository.getDiscussion(2)
            // then
            actual.onSuccess {
                assertThat(it).isEqualTo(expected)
            }
        }

    @Test
    fun `최신 토론을 페이지 단위로 가져온다`() =
        runTest {
            // given
            val pageSize = 10
            val firstCursor: String? = null

            // when
            val firstPageResult = defaultDiscussionRepository.getLatestDiscussions(pageSize, firstCursor)
            firstPageResult.onSuccess { page ->
                assertThat(page.discussions.size).isEqualTo(pageSize)
                assertThat(page.pageInfo.hasNext).isTrue()
            }

            val nextCursor = (firstPageResult as NetworkResult.Success).data.pageInfo.nextCursor
            val secondPageResult = defaultDiscussionRepository.getLatestDiscussions(pageSize, nextCursor)

            // then
            secondPageResult.onSuccess { page ->
                assertThat(page.discussions.size).isEqualTo(pageSize)
            }
        }

    @Test
    fun `마지막 페이지에서는 hasNext가 false이다`() =
        runTest {
            // given
            val pageSize = 50
            val cursor: String? = null

            // when
            val firstPage = defaultDiscussionRepository.getLatestDiscussions(pageSize, cursor)
            firstPage as NetworkResult.Success
            val lastCursor = firstPage.data.pageInfo.nextCursor
            val lastPageResult = defaultDiscussionRepository.getLatestDiscussions(pageSize, lastCursor)

            // then
            lastPageResult.onSuccess { page ->
                assertThat(page.pageInfo.hasNext).isFalse()
            }
        }
}
