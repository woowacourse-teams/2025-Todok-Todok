package com.example.todoktodok.data.repository

import com.example.domain.model.Comment
import com.example.todoktodok.data.datasource.CommentDataSource
import com.example.todoktodok.data.datasource.DefaultCommentDataSource
import com.example.todoktodok.fixture.COMMENTS
import com.example.todoktodok.fixture.USERS
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class DefaultCommentRepositoryTest {
    private lateinit var commentDataSource: CommentDataSource
    private lateinit var defaultCommentRepository: DefaultCommentRepository

    @BeforeEach
    fun setUp() {
        commentDataSource = DefaultCommentDataSource()
        defaultCommentRepository = DefaultCommentRepository(commentDataSource)
    }

    @Test
    fun `토론방 Id을 통해 댓글들을 반환한다`() {
        // given
        val expected = COMMENTS
        // when
        val comments = defaultCommentRepository.getCommentsByDiscussionRoomId(0)
        // then
        assertThat(comments).isEqualTo(expected)
    }

    @Test
    fun `댓글을 추가한다`() {
        // given
        val expected = Comment(6, "수고 많으셨습니다다.", USERS[4], LocalDateTime.of(2024, 7, 20, 22, 15))

        // when
        defaultCommentRepository.saveComment(
            Comment(6, "수고 많으셨습니다다.", USERS[4], LocalDateTime.of(2024, 7, 20, 22, 15)),
        )
        // then
        assertThat(defaultCommentRepository.getCommentsByDiscussionRoomId(1).last()).isEqualTo(
            expected,
        )
    }
}
