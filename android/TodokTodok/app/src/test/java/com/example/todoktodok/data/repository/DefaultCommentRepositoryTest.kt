package com.example.todoktodok.data.repository

import com.example.domain.model.Comment
import com.example.domain.model.member.Nickname
import com.example.domain.model.member.User
import com.example.todoktodok.data.datasource.comment.CommentRemoteDataSource
import com.example.todoktodok.fake.datasource.FakeCommentRemoteDataSource
import com.example.todoktodok.fixture.COMMENTS
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class DefaultCommentRepositoryTest {
    private lateinit var commentRemoteDataSource: CommentRemoteDataSource
    private lateinit var defaultCommentRepository: DefaultCommentRepository

    @BeforeEach
    fun setUp() {
        commentRemoteDataSource = FakeCommentRemoteDataSource()
        defaultCommentRepository = DefaultCommentRepository(commentRemoteDataSource)
    }

    @Test
    fun `토론방 Id을 통해 댓글들을 반환한다`() =
        runTest {
            // given
            val expected = COMMENTS
            // when
            val comments = defaultCommentRepository.getCommentsByDiscussionRoomId(0)
            // then
            assertThat(comments).isEqualTo(expected)
        }

    @Test
    fun `댓글을 추가한다`() =
        runTest {
            // given
            val expected =
                Comment(
                    100,
                    "수고 많으셨습니다다.",
                    User(1, Nickname("동전")),
                    LocalDateTime.of(2000, 7, 3, 10, 21),
                )
            // when
            defaultCommentRepository.saveComment(10, "수고 많으셨습니다다.")
            // then
            assertThat(defaultCommentRepository.getCommentsByDiscussionRoomId(1).last()).isEqualTo(
                expected,
            )
        }
}
