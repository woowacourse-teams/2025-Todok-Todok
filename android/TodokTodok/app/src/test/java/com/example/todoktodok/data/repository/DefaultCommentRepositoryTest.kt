package com.example.todoktodok.data.repository

import com.example.todoktodok.COMMENTS
import com.example.todoktodok.data.datasource.CommentDataSource
import com.example.todoktodok.data.datasource.DefaultCommentDataSource
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DefaultCommentRepositoryTest {
    private lateinit var commentDataSource: CommentDataSource
    private lateinit var defaultCommentRepository: DefaultCommentRepository

    @BeforeEach
    fun setUp() {
        commentDataSource = DefaultCommentDataSource()
        defaultCommentRepository = DefaultCommentRepository(commentDataSource)
    }

    @Test
    fun getCommentByDiscussionRoomId() {
        // given
        val expected = COMMENTS
        // when
        val comments = defaultCommentRepository.getCommentsByDiscussionRoomId(0)
        // then
        assertThat(comments).isEqualTo(expected)
    }
}
