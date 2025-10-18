package com.team.todoktodok.data.repository

import com.team.domain.model.LikeStatus
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
        defaultDiscussionRepository =
            DefaultDiscussionRepository(discussionRemoteDataSource, discussionLocalDataSource)
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
            val firstPageResult =
                defaultDiscussionRepository.getLatestDiscussions(pageSize, firstCursor)
            firstPageResult.onSuccess { page ->
                assertThat(page.discussions.size).isEqualTo(pageSize)
                assertThat(page.pageInfo.hasNext).isTrue()
            }

            val nextCursor = (firstPageResult as NetworkResult.Success).data.pageInfo.nextCursor
            val secondPageResult =
                defaultDiscussionRepository.getLatestDiscussions(pageSize, nextCursor)

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
            val lastPageResult =
                defaultDiscussionRepository.getLatestDiscussions(pageSize, lastCursor)

            // then
            lastPageResult.onSuccess { page ->
                assertThat(page.pageInfo.hasNext).isFalse()
            }
        }

    @Test
    fun `toggleLike를 호출하면 LIKE 이후 UNLIKE 시퀀스가 반환되고 카운트가 왕복한다`() =
        runTest {
            // given
            val targetId = 2L

            // when
            val likeRes = defaultDiscussionRepository.toggleLike(targetId)
            val afterLike = defaultDiscussionRepository.getDiscussion(targetId)

            val unlikeRes = defaultDiscussionRepository.toggleLike(targetId)
            val afterUnlike = defaultDiscussionRepository.getDiscussion(targetId)

            // then
            likeRes.onSuccess { action ->
                assertThat(action).isEqualTo(LikeStatus.LIKE)
            }
            afterLike.onSuccess { d ->
                assertThat(d.isLikedByMe).isTrue()
                assertThat(d.likeCount).isEqualTo(1)
            }

            unlikeRes.onSuccess { action ->
                assertThat(action).isEqualTo(LikeStatus.UNLIKE)
            }
            afterUnlike.onSuccess { d ->
                assertThat(d.isLikedByMe).isFalse()
                assertThat(d.likeCount).isEqualTo(0)
            }
        }

    @Test
    fun `toggleLike 후 fetch 결과와 동일 아이템 상태가 일치한다`() =
        runTest {
            // given
            val targetId = 3L
            defaultDiscussionRepository
                .toggleLike(targetId)
                .onSuccess { assertThat(it).isEqualTo(LikeStatus.LIKE) }

            // when
            val detail = defaultDiscussionRepository.getDiscussion(targetId)
            // then
            detail.onSuccess { d ->
                assertThat(d.isLikedByMe).isTrue()
                assertThat(d.likeCount).isEqualTo(1)
            }
        }

    @Test
    fun `reportDiscussion는 존재하는 ID면 성공, 없으면 Failure를 반환한다`() =
        runTest {
            // given
            val okId = 1L
            val badId = 9999L

            // when
            val ok = defaultDiscussionRepository.reportDiscussion(okId, reason = "스팸")
            val bad = defaultDiscussionRepository.reportDiscussion(badId, reason = "스팸")
            assertThat(ok).isInstanceOf(NetworkResult.Success::class.java)
            assertThat(bad).isInstanceOf(NetworkResult.Failure::class.java)
        }

    @Test
    fun `활성 토론을 페이지 단위로 가져온다`() =
        runTest {
            // given
            val pageSize = 2
            val period = 7
            val firstCursor: String? = null

            // when
            val firstPageResult = defaultDiscussionRepository.getActivatedDiscussion(period, pageSize, firstCursor)

            // then
            firstPageResult.onSuccess { page ->
                assertThat(page.discussions.size).isEqualTo(pageSize)
                assertThat(page.pageInfo.hasNext).isTrue()
            }

            // given
            val nextCursor = (firstPageResult as NetworkResult.Success).data.pageInfo.nextCursor
            // when
            val secondPageResult = defaultDiscussionRepository.getActivatedDiscussion(period, pageSize, nextCursor)
            // then
            secondPageResult.onSuccess { page ->
                assertThat(page.discussions.size).isEqualTo(2)
            }

            // given
            val lastCursor = (secondPageResult as NetworkResult.Success).data.pageInfo.nextCursor
            // when
            val lastPageResult = defaultDiscussionRepository.getActivatedDiscussion(period, pageSize, lastCursor)
            // then
            lastPageResult.onSuccess { page ->
                assertThat(page.pageInfo.hasNext).isFalse()
            }
        }

    @Test
    fun `좋아요 누른 토론만 페이지 단위로 가져온다`() =
        runTest {
            // given
            val targetIds = listOf(1L, 3L, 5L)
            targetIds.forEach { defaultDiscussionRepository.toggleLike(it) }

            val pageSize = 2
            val firstCursor: String? = null

            // when
            val firstPageResult = defaultDiscussionRepository.getLikedDiscussion(pageSize, firstCursor)

            // then
            firstPageResult.onSuccess { page ->
                assertThat(page.discussions.size).isEqualTo(pageSize)
                assertThat(page.discussions.all { it.isLikedByMe }).isTrue()
                assertThat(page.pageInfo.hasNext).isTrue()
            }

            // given
            val nextCursor = (firstPageResult as NetworkResult.Success).data.pageInfo.nextCursor

            // when
            val secondPageResult = defaultDiscussionRepository.getLikedDiscussion(pageSize, nextCursor)

            // then
            secondPageResult.onSuccess { page ->
                assertThat(page.discussions.size).isEqualTo(1)
                assertThat(page.discussions.all { it.isLikedByMe }).isTrue()
                assertThat(page.pageInfo.hasNext).isFalse()
            }
        }

    @Test
    fun `키워드로 토론방을 검색하면 해당 키워드 포함 토론만 반환한다`() =
        runTest {
            // given
            val keyword = "코루틴"

            // when
            val result = defaultDiscussionRepository.getSearchDiscussion(keyword)

            // then
            result.onSuccess { discussions ->
                assertThat(discussions).isNotEmpty
                assertThat(
                    discussions.all {
                        it.discussionTitle.contains(keyword) || it.discussionOpinion.contains(keyword)
                    },
                ).isTrue()
            }
        }

    @Test
    fun `키워드가 없으면 빈 리스트를 반환한다`() =
        runTest {
            // given
            val keyword = "에베베베"

            // when
            val result = defaultDiscussionRepository.getSearchDiscussion(keyword)

            // then
            result.onSuccess { discussions ->
                assertThat(discussions).isEmpty()
            }
        }
}
