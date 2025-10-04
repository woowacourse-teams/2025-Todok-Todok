package com.team.todoktodok.presentation.vm

import app.cash.turbine.test
import com.team.domain.ConnectivityObserver
import com.team.domain.model.Book
import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.member.MemberId
import com.team.domain.model.member.Profile
import com.team.domain.repository.MemberRepository
import com.team.todoktodok.CoroutinesTestExtension
import com.team.todoktodok.InstantTaskExecutorExtension
import com.team.todoktodok.presentation.compose.my.vm.MyProfileViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(CoroutinesTestExtension::class)
@ExtendWith(InstantTaskExecutorExtension::class)
class MyProfileViewModelTest {
    private lateinit var memberRepository: MemberRepository
    private lateinit var connectivityObserver: ConnectivityObserver
    private lateinit var viewModel: MyProfileViewModel

    @BeforeEach
    fun setup() {
        memberRepository = mockk()
        connectivityObserver = mockk()
        every { connectivityObserver.subscribe() } returns emptyFlow()
        every { connectivityObserver.value() } returns ConnectivityObserver.Status.Available
        viewModel = MyProfileViewModel(memberRepository, connectivityObserver)
    }

    @Test
    fun `프로필 정보를 불러온다`() =
        runTest {
            // Given
            val profile =
                Profile(
                    memberId = 1,
                    nickname = "페토",
                    message = "안녕하세요",
                    profileImage = "",
                )
            coEvery { memberRepository.getProfile(any()) } returns NetworkResult.Success(profile)

            // When
            viewModel.loadProfile()

            // Then
            viewModel.uiState.test {
                val item = awaitItem()
                assertEquals(profile, item.profile)
            }
        }

    @Test
    fun `내가 활동한 책 목록을 불러온다`() =
        runTest {
            // Given
            val books =
                listOf(
                    Book(1, "Book 1", "페토", ""),
                    Book(2, "Book 2", "정페토", ""),
                )
            coEvery { memberRepository.getMemberBooks(MemberId.Mine) } returns NetworkResult.Success(books)

            // When
            viewModel.loadMyBooks()

            // Then
            viewModel.uiState.test {
                val item = awaitItem()
                assertEquals(books, item.activatedBooks.books)
            }
        }
}
