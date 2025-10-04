package com.team.todoktodok.presentation.vm

import app.cash.turbine.test
import com.team.domain.ConnectivityObserver
import com.team.domain.model.Book
import com.team.domain.model.Discussion
import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.member.Nickname
import com.team.domain.model.member.Profile
import com.team.domain.model.member.User
import com.team.domain.repository.MemberRepository
import com.team.todoktodok.CoroutinesTestExtension
import com.team.todoktodok.InstantTaskExecutorExtension
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionUiState
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
import java.time.LocalDateTime

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
    fun `프로필과 책, 토론 정보를 한번에 불러온다`() =
        runTest {
            // Given
            val profile = Profile(1, "페토", "안녕하세요", "")
            val books = listOf(Book(1, "Book1", "페토", ""), Book(2, "Book2", "정페토", ""))
            val discussions =
                listOf(
                    Discussion(
                        id = 1L,
                        discussionTitle = "JPA 성능 최적화",
                        book = Book(1L, "자바 ORM 표준 JPA 프로그래밍", "김영한", ""),
                        writer = User(1L, Nickname("홍길동"), ""),
                        createAt = LocalDateTime.of(2025, 7, 12, 12, 0),
                        discussionOpinion =
                            "응집도와 결합도가 어떤 차이를 가지는 지에 대한 다른 분들의 생각이 궁금합니다." +
                                "응집도는 내부에 얼마나 비슷한 책임들이 모여있는가. 얼마나 연관있는 멤버들이 똘똘 뭉쳐있는가" +
                                "응집도는 내부에 얼마나 비슷한 책임들이 모여있는가. 얼마나 연관있는 멤버들이 똘똘 뭉쳐있는가",
                        likeCount = 0,
                        commentCount = 0,
                        viewCount = 0,
                        isLikedByMe = false,
                    ),
                )

            coEvery { memberRepository.getProfile(any()) } returns NetworkResult.Success(profile)
            coEvery { memberRepository.getMemberBooks(any()) } returns NetworkResult.Success(books)
            coEvery {
                memberRepository.getMemberDiscussionRooms(
                    any(),
                    any(),
                )
            } returns NetworkResult.Success(discussions)

            // When
            viewModel.loadInitialProfile()

            // Then
            viewModel.uiState.test {
                val item = awaitItem()
                assertEquals(profile, item.profile)
                assertEquals(books, item.activatedBooks.books)
                assertEquals(
                    discussions.map { DiscussionUiState(it) },
                    item.participatedDiscussions.discussions,
                )
            }
        }
}
