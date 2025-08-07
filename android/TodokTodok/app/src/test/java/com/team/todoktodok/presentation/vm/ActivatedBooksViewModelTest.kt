package com.team.todoktodok.presentation.vm

import com.team.domain.repository.MemberRepository
import com.team.todoktodok.CoroutinesTestExtension
import com.team.todoktodok.InstantTaskExecutorExtension
import com.team.todoktodok.fixture.BOOKS_FIXTURES
import com.team.todoktodok.presentation.view.profile.activated.vm.ActivatedBooksViewModel
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantTaskExecutorExtension::class)
@ExtendWith(CoroutinesTestExtension::class)
class ActivatedBooksViewModelTest {
    private lateinit var mockMemberRepository: MemberRepository
    private lateinit var viewModel: ActivatedBooksViewModel

    @BeforeEach
    fun setUp() {
        mockMemberRepository = mockk(relaxed = true)
        viewModel = ActivatedBooksViewModel(mockMemberRepository)
    }

    @Test
    fun `유저가 활동했던 도서를 가져온다`() =
        runTest {
            // given
            val memberId = 1L
            coEvery { mockMemberRepository.getMemberBooks(any()) } returns BOOKS_FIXTURES

            // when
            viewModel.loadActivatedBooks(memberId)

            // then
            viewModel.books.value shouldBe BOOKS_FIXTURES
        }
}
