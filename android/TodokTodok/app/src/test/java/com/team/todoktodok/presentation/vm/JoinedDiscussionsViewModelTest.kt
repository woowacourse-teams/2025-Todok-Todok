package com.team.todoktodok.presentation.vm

import com.team.domain.model.member.MemberDiscussion
import com.team.domain.repository.MemberRepository
import com.team.todoktodok.CoroutinesTestExtension
import com.team.todoktodok.InstantTaskExecutorExtension
import com.team.todoktodok.presentation.view.profile.joined.vm.JoinedDiscussionsViewModel
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(CoroutinesTestExtension::class)
@ExtendWith(InstantTaskExecutorExtension::class)
class JoinedDiscussionsViewModelTest {
    private lateinit var repository: MemberRepository
    private lateinit var viewModel: JoinedDiscussionsViewModel

    @BeforeEach
    fun setUp() {
        repository = mockk(relaxed = true)
        viewModel = JoinedDiscussionsViewModel(repository)
    }

    @Test
    fun `사용자가 생성했던 토론 목록을 불러온다`() =
        runTest {
            // given
            val discussions = emptyList<MemberDiscussion>()
            coEvery { repository.getMemberDiscussionRooms(any(), any()) } returns discussions

            // when
            viewModel.loadDiscussions(1)

            // then
            viewModel.discussion.value shouldBe discussions
        }
}
