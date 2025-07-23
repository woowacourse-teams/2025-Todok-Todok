package com.example.todoktodok.presentation.vm

import androidx.lifecycle.SavedStateHandle
import com.example.domain.model.Comment
import com.example.domain.model.member.Nickname
import com.example.domain.model.member.User
import com.example.domain.repository.CommentRepository
import com.example.domain.repository.DiscussionRoomRepository
import com.example.todoktodok.InstantTaskExecutorExtension
import com.example.todoktodok.ext.getOrAwaitValue
import com.example.todoktodok.fake.FakeCommentRepository
import com.example.todoktodok.fake.FakeDiscussionRoomRepository
import com.example.todoktodok.fixture.COMMENTS
import com.example.todoktodok.fixture.DISCUSSION_ROOMS
import com.example.todoktodok.presentation.view.discussion.detail.vm.DiscussionRoomDetailViewModel
import com.example.todoktodok.presentation.view.discussion.detail.vm.DiscussionRoomDetailViewModel.Companion.KEY_DISCUSSION_ID
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime

@ExtendWith(InstantTaskExecutorExtension::class)
class DiscussionRoomDetailViewModelTest {
    private lateinit var discussionRoomDetailViewModel: DiscussionRoomDetailViewModel
    private lateinit var discussionRoomRepository: DiscussionRoomRepository
    private lateinit var commentRepository: CommentRepository

    @BeforeEach
    fun setUp() {
        val state = SavedStateHandle(mapOf(KEY_DISCUSSION_ID to 2L))
        discussionRoomRepository = FakeDiscussionRoomRepository()
        commentRepository = FakeCommentRepository()
        discussionRoomDetailViewModel =
            DiscussionRoomDetailViewModel(
                state,
                discussionRoomRepository,
                commentRepository,
            )
    }

    @Test
    fun `저장소에서 불러온 토론방을 가진다`() {
        // given
        val expected = DISCUSSION_ROOMS.find { it.id == 2L }
        // then
        assertThat(discussionRoomDetailViewModel.discussionRoom.getOrAwaitValue()).isEqualTo(
            expected,
        )
    }

    @Test
    fun `저장소에서 불러온 댓글들을 가진다`() {
        // given
        val expected = COMMENTS
        // then
        assertThat(discussionRoomDetailViewModel.comments.getOrAwaitValue()).isEqualTo(
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
        discussionRoomDetailViewModel.addComment(LocalDateTime.of(2000, 7, 3, 10, 21), "테스트가 어려워")
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
