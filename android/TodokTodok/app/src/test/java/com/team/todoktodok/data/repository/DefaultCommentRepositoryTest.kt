package com.team.todoktodok.data.repository

import com.team.domain.model.Comment
import com.team.domain.model.exception.onSuccess
import com.team.domain.model.member.Nickname
import com.team.domain.model.member.User
import com.team.todoktodok.data.datasource.comment.CommentRemoteDataSource
import com.team.todoktodok.fake.datasource.FakeCommentRemoteDataSource
import com.team.todoktodok.fixture.COMMENTS
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
            val expected = COMMENTS.sortedBy { it.createAt }
            // when
            val comments = defaultCommentRepository.getCommentsByDiscussionId(0)
            // then
            comments.onSuccess {
                assertThat(it).isEqualTo(expected)
            }
        }
}
