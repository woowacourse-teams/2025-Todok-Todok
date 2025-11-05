package com.team.data.repository

import com.team.data.datasource.reply.ReplyRemoteDataSource
import com.team.data.network.model.LikeAction
import com.team.data.network.response.comment.ReplyResponse
import com.team.data.network.response.discussion.MemberResponse
import com.team.domain.model.LikeStatus
import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.exception.TodokTodokExceptions
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DefaultReplyRepositoryTest {
    private lateinit var replyRemoteDataSource: ReplyRemoteDataSource
    private lateinit var defaultReplyRepository: DefaultReplyRepository

    @BeforeEach
    fun setUp() {
        replyRemoteDataSource = mockk()
        defaultReplyRepository = DefaultReplyRepository(replyRemoteDataSource)
    }

    @Test
    fun `대댓글 목록 조회 성공 시 도메인 모델로 변환된다`() =
        runTest {
            // given
            val discussionId = 1L
            val commentId = 1L
            val replyResponses =
                listOf(
                    ReplyResponse(
                        replyId = 1L,
                        content = "대댓글 내용",
                        createdAt = "2024-01-01T00:00:00",
                        member =
                            MemberResponse(
                                memberId = 1L,
                                nickname = "테스트 유저",
                                profileImage = "https://example.com/profile.jpg",
                            ),
                        likeCount = 5,
                        isLikedByMe = true,
                    ),
                )
            coEvery {
                replyRemoteDataSource.fetchReplies(discussionId, commentId)
            } returns NetworkResult.Success(replyResponses)

            // when
            val result = defaultReplyRepository.getReplies(discussionId, commentId)

            // then
            assertTrue(result is NetworkResult.Success)
            val replies = (result as NetworkResult.Success).data
            assertEquals(1, replies.size)
            assertEquals("대댓글 내용", replies[0].content)
            assertEquals(5, replies[0].likeCount)
            assertEquals(true, replies[0].isLikedByMe)
        }

    @Test
    fun `대댓글 저장 성공 시 Unit을 반환한다`() =
        runTest {
            // given
            val discussionId = 1L
            val commentId = 1L
            val content = "새 대댓글"
            coEvery {
                replyRemoteDataSource.saveReply(discussionId, commentId, content)
            } returns NetworkResult.Success(Unit)

            // when
            val result = defaultReplyRepository.saveReply(discussionId, commentId, content)

            // then
            assertTrue(result is NetworkResult.Success)
            coVerify { replyRemoteDataSource.saveReply(discussionId, commentId, content) }
        }

    @Test
    fun `대댓글 좋아요 토글 성공 시 LikeStatus를 반환한다`() =
        runTest {
            // given
            val discussionId = 1L
            val commentId = 1L
            val replyId = 1L
            coEvery {
                replyRemoteDataSource.toggleLike(discussionId, commentId, replyId)
            } returns NetworkResult.Success(LikeAction.LIKE)

            // when
            val result = defaultReplyRepository.toggleLike(discussionId, commentId, replyId)

            // then
            assertTrue(result is NetworkResult.Success)
            val likeStatus = (result as NetworkResult.Success).data
            assertEquals(LikeStatus.LIKE, likeStatus)
        }

    @Test
    fun `대댓글 수정 성공 시 Unit을 반환한다`() =
        runTest {
            // given
            val discussionId = 1L
            val commentId = 1L
            val replyId = 1L
            val content = "수정된 대댓글"
            coEvery {
                replyRemoteDataSource.updateReply(discussionId, commentId, replyId, content)
            } returns NetworkResult.Success(Unit)

            // when
            val result = defaultReplyRepository.updateReply(discussionId, commentId, replyId, content)

            // then
            assertTrue(result is NetworkResult.Success)
            coVerify { replyRemoteDataSource.updateReply(discussionId, commentId, replyId, content) }
        }

    @Test
    fun `대댓글 삭제 성공 시 Unit을 반환한다`() =
        runTest {
            // given
            val discussionId = 1L
            val commentId = 1L
            val replyId = 1L
            coEvery {
                replyRemoteDataSource.deleteReply(discussionId, commentId, replyId)
            } returns NetworkResult.Success(Unit)

            // when
            val result = defaultReplyRepository.deleteReply(discussionId, commentId, replyId)

            // then
            assertTrue(result is NetworkResult.Success)
            coVerify { replyRemoteDataSource.deleteReply(discussionId, commentId, replyId) }
        }

    @Test
    fun `대댓글 신고 성공 시 Unit을 반환한다`() =
        runTest {
            // given
            val discussionId = 1L
            val commentId = 1L
            val replyId = 1L
            val reason = "스팸"
            coEvery {
                replyRemoteDataSource.report(discussionId, commentId, replyId, reason)
            } returns NetworkResult.Success(Unit)

            // when
            val result = defaultReplyRepository.report(discussionId, commentId, replyId, reason)

            // then
            assertTrue(result is NetworkResult.Success)
            coVerify { replyRemoteDataSource.report(discussionId, commentId, replyId, reason) }
        }

    @Test
    fun `원격 데이터소스 실패 시 실패가 전파된다`() =
        runTest {
            // given
            val discussionId = 1L
            val commentId = 1L
            coEvery {
                replyRemoteDataSource.fetchReplies(discussionId, commentId)
            } returns NetworkResult.Failure(TodokTodokExceptions.UnknownException(Exception("Network error")))

            // when
            val result = defaultReplyRepository.getReplies(discussionId, commentId)

            // then
            assertTrue(result is NetworkResult.Failure)
        }
}
