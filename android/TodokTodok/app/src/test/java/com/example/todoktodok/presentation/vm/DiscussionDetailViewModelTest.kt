package com.example.todoktodok.presentation.vm

import androidx.lifecycle.SavedStateHandle
import com.example.domain.model.Comment
import com.example.domain.model.member.Nickname
import com.example.domain.model.member.User
import com.example.domain.repository.CommentRepository
import com.example.domain.repository.DiscussionRepository
import com.example.todoktodok.InstantTaskExecutorExtension
import com.example.todoktodok.ext.getOrAwaitValue
import com.example.todoktodok.fake.FakeCommentRepository
import com.example.todoktodok.fake.FakeDiscussionRepository
import com.example.todoktodok.fixture.COMMENTS
import com.example.todoktodok.fixture.DISCUSSIONS
import com.example.todoktodok.presentation.view.discussion.detail.vm.DiscussionDetailViewModel
import com.example.todoktodok.presentation.view.discussion.detail.vm.DiscussionDetailViewModel.Companion.KEY_DISCUSSION_ID
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime

@ExtendWith(InstantTaskExecutorExtension::class)
class DiscussionDetailViewModelTest {
    private lateinit var discussionDetailViewModel: DiscussionDetailViewModel
    private lateinit var discussionRepository: DiscussionRepository
    private lateinit var commentRepository: CommentRepository

    @BeforeEach
    fun setUp() {
        val state = SavedStateHandle(mapOf(KEY_DISCUSSION_ID to 2L))
        discussionRepository = FakeDiscussionRepository()
        commentRepository = FakeCommentRepository()
        discussionDetailViewModel =
            DiscussionDetailViewModel(
                state,
                discussionRepository,
                commentRepository,
            )
    }

    @Test
    fun `저장소에서 불러온 토론방을 가진다`() {
        // given
        val expected = DISCUSSIONS.find { it.id == 2L }
        // then
        assertThat(discussionDetailViewModel.discussion.getOrAwaitValue()).isEqualTo(
            expected,
        )
    }

    @Test
    fun `저장소에서 불러온 댓글들을 가진다`() {
        // given
        val expected = COMMENTS
        // then
        assertThat(discussionDetailViewModel.comments.getOrAwaitValue()).isEqualTo(
            expected,
        )
    }

    @Test
    fun `저장소에 댓글을 저장한다`() {
        // given
        val expected =
            Comment(
                100,
                "테스트가 어려워",
                User(1, Nickname("동전")),
                LocalDateTime.of(2000, 7, 3, 10, 21),
            )
        // when
        discussionDetailViewModel.addComment(LocalDateTime.of(2000, 7, 3, 10, 21), "테스트가 어려워")
        // then
        assertThat(
            commentRepository
                .getCommentsByDiscussionRoomId(1)
                .find { it.id == 100L },
        ).isEqualTo(
            expected,
        )
    }
}
