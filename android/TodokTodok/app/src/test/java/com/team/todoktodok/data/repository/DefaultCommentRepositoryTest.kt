package com.team.todoktodok.data.repository

import com.team.domain.model.LikeStatus
import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.exception.TodokTodokExceptions
import com.team.domain.repository.CommentRepository
import com.team.todoktodok.data.datasource.comment.CommentRemoteDataSource
import com.team.todoktodok.data.network.model.LikeAction
import com.team.todoktodok.data.network.request.CommentRequest
import com.team.todoktodok.fixture.COMMENT_RESPONSES
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DefaultCommentRepositoryTest {
    private lateinit var commentRemoteDataSource: CommentRemoteDataSource
    private lateinit var commentRepository: CommentRepository

    @BeforeEach
    fun setup() {
        commentRemoteDataSource = mockk(relaxed = true)
        commentRepository = DefaultCommentRepository(commentRemoteDataSource)
    }

    @Test
    fun `getComment - 성공 시 response를 domain으로 매핑해 반환`() =
        runTest {
            // given
            val response = COMMENT_RESPONSES[0]
            coEvery { commentRemoteDataSource.fetchComment(DISCUSSION_ID, COMMENT_ID) } returns
                NetworkResult.Success(response)

            // when
            val result = commentRepository.getComment(DISCUSSION_ID, COMMENT_ID)

            // then
            assertThat(result).isInstanceOf(NetworkResult.Success::class.java)
            val domain = (result as NetworkResult.Success).data
            assertThat(domain.id).isEqualTo(COMMENT_ID)
            assertThat(domain.content).isEqualTo("정말 좋은 글이에요!")
            coVerify(exactly = 1) {
                commentRemoteDataSource.fetchComment(
                    DISCUSSION_ID,
                    COMMENT_ID,
                )
            }
        }

    @Test
    fun `getComment - 실패 시 Failure 그대로 전달`() =
        runTest {
            val ex = TodokTodokExceptions.EmptyBodyException
            coEvery {
                commentRemoteDataSource.fetchComment(
                    DISCUSSION_ID,
                    COMMENT_ID,
                )
            } returns NetworkResult.Failure(ex)

            val result = commentRepository.getComment(DISCUSSION_ID, COMMENT_ID)

            assertThat(result).isEqualTo(NetworkResult.Failure(ex))
            coVerify { commentRemoteDataSource.fetchComment(DISCUSSION_ID, COMMENT_ID) }
        }

    @Test
    fun `getCommentsByDiscussionId - 성공 시 도메인 매핑 후 createAt 오름차순 정렬`() =
        runTest {
            coEvery { commentRemoteDataSource.fetchCommentsByDiscussionId(DISCUSSION_ID) } returns
                NetworkResult.Success(COMMENT_RESPONSES)

            // when
            val result = commentRepository.getCommentsByDiscussionId(DISCUSSION_ID)

            // then
            assertThat(result).isInstanceOf(NetworkResult.Success::class.java)
            val list = (result as NetworkResult.Success).data
            assertThat(list.map { it.id }).containsExactly(1L, 2L, 3L, 4L, 5L)
            assertThat(list.map { it.createAt }).isSorted
            coVerify { commentRemoteDataSource.fetchCommentsByDiscussionId(DISCUSSION_ID) }
        }

    @Test
    fun `getCommentsByDiscussionId - 실패 시 Failure 그대로 전달`() =
        runTest {
            val ex = TodokTodokExceptions.EmptyBodyException
            coEvery { commentRemoteDataSource.fetchCommentsByDiscussionId(DISCUSSION_ID) } returns
                NetworkResult.Failure(
                    ex,
                )

            val result = commentRepository.getCommentsByDiscussionId(DISCUSSION_ID)

            assertThat(result).isEqualTo(NetworkResult.Failure(ex))
            coVerify { commentRemoteDataSource.fetchCommentsByDiscussionId(DISCUSSION_ID) }
        }

    @Test
    fun `saveComment - 성공 시 Unit 성공 전달 및 원본 파라미터 그대로 전달`() =
        runTest {
            coEvery {
                commentRemoteDataSource.saveComment(
                    DISCUSSION_ID,
                    CommentRequest("저장 성공"),
                )
            } returns NetworkResult.Success(Unit)

            val result = commentRepository.saveComment(DISCUSSION_ID, "저장 성공")

            assertThat(result).isEqualTo(NetworkResult.Success(Unit))
            coVerify { commentRemoteDataSource.saveComment(DISCUSSION_ID, CommentRequest("저장 성공")) }
        }

    @Test
    fun `saveComment - 실패 시 Failure 그대로 전달`() =
        runTest {
            val ex = TodokTodokExceptions.EmptyBodyException
            coEvery {
                commentRemoteDataSource.saveComment(
                    DISCUSSION_ID,
                    CommentRequest("저장 실패"),
                )
            } returns NetworkResult.Failure(ex)

            val result = commentRepository.saveComment(DISCUSSION_ID, "저장 실패")

            assertThat(result).isEqualTo(NetworkResult.Failure(ex))
        }

    @Test
    fun `toggleLike - 성공 시 상태 매핑해 반환`() =
        runTest {
            coEvery { commentRemoteDataSource.toggleLike(DISCUSSION_ID, COMMENT_ID) } returns
                NetworkResult.Success(
                    LikeAction.LIKE,
                )

            val result = commentRepository.toggleLike(DISCUSSION_ID, COMMENT_ID)

            assertThat(result).isInstanceOf(NetworkResult.Success::class.java)
            val status = (result as NetworkResult.Success).data
            assertThat(status).isEqualTo(LikeStatus.LIKE)
            coVerify { commentRemoteDataSource.toggleLike(DISCUSSION_ID, COMMENT_ID) }
        }

    @Test
    fun `toggleLike - 실패 시 Failure 그대로 전달`() =
        runTest {
            val ex = TodokTodokExceptions.EmptyBodyException
            coEvery {
                commentRemoteDataSource.toggleLike(
                    DISCUSSION_ID,
                    COMMENT_ID,
                )
            } returns NetworkResult.Failure(ex)

            val result = commentRepository.toggleLike(DISCUSSION_ID, COMMENT_ID)

            assertThat(result).isEqualTo(NetworkResult.Failure(ex))
        }

    @Test
    fun `updateComment - 성공 시 Unit 성공 전달 및 파라미터 전달 확인`() =
        runTest {
            coEvery {
                commentRemoteDataSource.updateComment(
                    DISCUSSION_ID,
                    COMMENT_ID,
                    "수정",
                )
            } returns NetworkResult.Success(Unit)

            val result = commentRepository.updateComment(DISCUSSION_ID, COMMENT_ID, "수정")

            assertThat(result).isEqualTo(NetworkResult.Success(Unit))
            coVerify { commentRemoteDataSource.updateComment(DISCUSSION_ID, COMMENT_ID, "수정") }
        }

    @Test
    fun `updateComment - 실패 시 Failure 그대로 전달`() =
        runTest {
            val ex = TodokTodokExceptions.EmptyBodyException
            coEvery {
                commentRemoteDataSource.updateComment(
                    DISCUSSION_ID,
                    COMMENT_ID,
                    "에러",
                )
            } returns NetworkResult.Failure(ex)

            val result = commentRepository.updateComment(DISCUSSION_ID, COMMENT_ID, "에러")

            assertThat(result).isEqualTo(NetworkResult.Failure(ex))
        }

    @Test
    fun `deleteComment - 성공 시 Unit 성공 전달`() =
        runTest {
            coEvery { commentRemoteDataSource.deleteComment(DISCUSSION_ID, COMMENT_ID) } returns
                NetworkResult.Success(
                    Unit,
                )

            val result = commentRepository.deleteComment(DISCUSSION_ID, COMMENT_ID)

            assertThat(result).isEqualTo(NetworkResult.Success(Unit))
            coVerify { commentRemoteDataSource.deleteComment(DISCUSSION_ID, COMMENT_ID) }
        }

    @Test
    fun `deleteComment - 실패 시 Failure 그대로 전달`() =
        runTest {
            val ex = TodokTodokExceptions.EmptyBodyException
            coEvery {
                commentRemoteDataSource.deleteComment(
                    DISCUSSION_ID,
                    COMMENT_ID,
                )
            } returns NetworkResult.Failure(ex)

            val result = commentRepository.deleteComment(DISCUSSION_ID, COMMENT_ID)

            assertThat(result).isEqualTo(NetworkResult.Failure(ex))
        }

    @Test
    fun `report - 성공 시 Unit 성공 전달`() =
        runTest {
            coEvery { commentRemoteDataSource.report(DISCUSSION_ID, COMMENT_ID, "스햄") } returns
                NetworkResult.Success(
                    Unit,
                )

            val result = commentRepository.report(DISCUSSION_ID, COMMENT_ID, "스햄")

            assertThat(result).isEqualTo(NetworkResult.Success(Unit))
            coVerify { commentRemoteDataSource.report(DISCUSSION_ID, COMMENT_ID, "스햄") }
        }

    @Test
    fun `report - 실패 시 Failure 그대로 전달`() =
        runTest {
            val ex = TodokTodokExceptions.EmptyBodyException
            coEvery {
                commentRemoteDataSource.report(
                    DISCUSSION_ID,
                    COMMENT_ID,
                    "이유",
                )
            } returns NetworkResult.Failure(ex)

            val result = commentRepository.report(DISCUSSION_ID, COMMENT_ID, "이유")

            assertThat(result).isEqualTo(NetworkResult.Failure(ex))
        }

    companion object {
        private const val DISCUSSION_ID = 10L
        private const val COMMENT_ID = 1L
    }
}
