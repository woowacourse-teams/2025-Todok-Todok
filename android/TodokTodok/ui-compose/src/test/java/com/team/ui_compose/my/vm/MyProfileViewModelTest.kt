package com.team.ui_compose.my.vm

import app.cash.turbine.test
import com.team.domain.ConnectivityObserver
import com.team.domain.model.Book
import com.team.domain.model.Discussion
import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.member.Profile
import com.team.domain.model.member.User
import com.team.domain.repository.DiscussionRepository
import com.team.domain.repository.MemberRepository
import com.team.domain.repository.TokenRepository
import com.team.ui_compose.CoroutinesTestExtension
import com.team.ui_compose.InstantTaskExecutorExtension
import com.team.ui_compose.discussion.model.DiscussionUiModel
import com.team.ui_compose.my.vm.MyProfileViewModel
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
    private lateinit var discussionRepository: DiscussionRepository
    private lateinit var tokenRepository: TokenRepository
    private lateinit var connectivityObserver: ConnectivityObserver
    private lateinit var viewModel: MyProfileViewModel

    @BeforeEach
    fun setup() {
        memberRepository = mockk()
        discussionRepository = mockk()
        connectivityObserver = mockk()
        tokenRepository = mockk()
        every { connectivityObserver.subscribe(any()) } returns emptyFlow()
        every { connectivityObserver.value() } returns ConnectivityObserver.Status.Available
        viewModel =
            MyProfileViewModel(
                memberRepository,
                discussionRepository,
                tokenRepository,
                connectivityObserver,
            )
    }

    @Test
    fun `프로필과 책, 토론 정보를 한번에 불러온다`() =
        runTest {
            // Given
            val profile = Profile(1, "페토", "안녕하세요", "")
            val books =
                listOf(
                    Book(1, "Book1", "페토", ""),
                    Book(2, "Book2", "정페토", ""),
                )
            val discussions =
                listOf(
                    Discussion(
                        id = 1L,
                        discussionTitle = "JPA 성능 최적화",
                        book = Book(1L, "자바 ORM 표준 JPA 프로그래밍", "김영한", ""),
                        writer = User(1L, "홍길동", ""),
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
            coEvery {
                discussionRepository.getLikedDiscussion()
            } returns NetworkResult.Success(discussions)
            coEvery { tokenRepository.getMemberId() } returns 1

            // When
            viewModel.loadInitialProfile()

            // Then
            viewModel.uiState.test {
                val item = awaitItem()
                assertEquals(profile, item.profile)
                assertEquals(books, item.activatedBooks.books)
                assertEquals(
                    discussions.map { DiscussionUiModel(it) },
                    item.participatedDiscussions.discussions,
                )
                assertEquals(
                    discussions.map { DiscussionUiModel(it) },
                    item.likedDiscussions.discussions,
                )
            }
        }

    @Test
    fun `toggleShowMyDiscussion이 프로필 표시를 토글한다`() =
        runTest {
            // When
            viewModel.toggleShowMyDiscussion(true)

            // Then
            viewModel.uiState.test {
                val item = awaitItem()
                assertEquals(true, item.participatedDiscussions.showMyDiscussion)
            }

            // When
            viewModel.toggleShowMyDiscussion(false)

            // Then
            viewModel.uiState.test {
                val item = awaitItem()
                assertEquals(false, item.participatedDiscussions.showMyDiscussion)
            }
        }

    @Test
    fun `modifyProfileImage 성공 시 프로필 이미지가 업데이트된다`() =
        runTest {
            // Given
            val profile = Profile(1, "페토", "안녕하세요", "")
            val newImageUrl = "https://example.com/new-image.png"
            coEvery { memberRepository.getProfile(any()) } returns NetworkResult.Success(profile)
            coEvery { memberRepository.getMemberBooks(any()) } returns NetworkResult.Success(emptyList())
            coEvery {
                memberRepository.getMemberDiscussionRooms(
                    any(),
                    any(),
                )
            } returns NetworkResult.Success(emptyList())
            coEvery {
                discussionRepository.getLikedDiscussion()
            } returns NetworkResult.Success(emptyList())
            coEvery { tokenRepository.getMemberId() } returns 1

            val imagePayload =
                com.team.domain.model.ImagePayload(
                    fileName = "profile.jpg",
                    mediaType = "image/jpeg",
                    openStream = { ByteArray(10).inputStream() },
                )
            coEvery { memberRepository.modifyProfileImage(imagePayload) } returns
                NetworkResult.Success(newImageUrl)

            viewModel.loadInitialProfile()

            // When
            viewModel.modifyProfileImage(imagePayload)

            // Then
            viewModel.uiState.test {
                val item = awaitItem()
                assertEquals(newImageUrl, item.profile.profileImage)
            }
        }

    @Test
    fun `modifyProfileImage 실패 시 에러 이벤트가 발생한다`() =
        runTest {
            // Given
            val profile = Profile(1, "페토", "안녕하세요", "")
            coEvery { memberRepository.getProfile(any()) } returns NetworkResult.Success(profile)
            coEvery { memberRepository.getMemberBooks(any()) } returns NetworkResult.Success(emptyList())
            coEvery {
                memberRepository.getMemberDiscussionRooms(
                    any(),
                    any(),
                )
            } returns NetworkResult.Success(emptyList())
            coEvery {
                discussionRepository.getLikedDiscussion()
            } returns NetworkResult.Success(emptyList())
            coEvery { tokenRepository.getMemberId() } returns 1

            val imagePayload =
                com.team.domain.model.ImagePayload(
                    fileName = "profile.jpg",
                    mediaType = "image/jpeg",
                    openStream = { ByteArray(10).inputStream() },
                )
            val exception = com.team.domain.model.exception.TodokTodokExceptions.EmptyBodyException
            coEvery { memberRepository.modifyProfileImage(imagePayload) } returns
                NetworkResult.Failure(exception)

            viewModel.loadInitialProfile()

            // When
            viewModel.modifyProfileImage(imagePayload)

            // Then
            viewModel.uiEvent.test {
                val event = awaitItem()
                assertEquals(
                    com.team.ui_compose.my.MyProfileUiEvent
                        .ShowErrorMessage(exception),
                    event,
                )
            }
        }

    @Test
    fun `loadProfile 실패 시 에러 이벤트가 발생한다`() =
        runTest {
            // Given
            val exception = com.team.domain.model.exception.TodokTodokExceptions.EmptyBodyException
            coEvery { memberRepository.getProfile(any()) } returns NetworkResult.Failure(exception)
            coEvery { memberRepository.getMemberBooks(any()) } returns NetworkResult.Success(emptyList())
            coEvery {
                memberRepository.getMemberDiscussionRooms(
                    any(),
                    any(),
                )
            } returns NetworkResult.Success(emptyList())
            coEvery {
                discussionRepository.getLikedDiscussion()
            } returns NetworkResult.Success(emptyList())
            coEvery { tokenRepository.getMemberId() } returns 1

            // When
            viewModel.loadInitialProfile()

            // Then
            viewModel.uiEvent.test {
                val event = awaitItem()
                assertEquals(
                    com.team.ui_compose.my.MyProfileUiEvent
                        .ShowErrorMessage(exception),
                    event,
                )
            }
        }

    @Test
    fun `loadMyBooks 실패 시 에러 이벤트가 발생한다`() =
        runTest {
            // Given
            val profile = Profile(1, "페토", "안녕하세요", "")
            val exception = com.team.domain.model.exception.TodokTodokExceptions.EmptyBodyException
            coEvery { memberRepository.getProfile(any()) } returns NetworkResult.Success(profile)
            coEvery { memberRepository.getMemberBooks(any()) } returns NetworkResult.Failure(exception)
            coEvery {
                memberRepository.getMemberDiscussionRooms(
                    any(),
                    any(),
                )
            } returns NetworkResult.Success(emptyList())
            coEvery {
                discussionRepository.getLikedDiscussion()
            } returns NetworkResult.Success(emptyList())
            coEvery { tokenRepository.getMemberId() } returns 1

            // When
            viewModel.loadInitialProfile()

            // Then
            viewModel.uiEvent.test {
                val event = awaitItem()
                assertEquals(
                    com.team.ui_compose.my.MyProfileUiEvent
                        .ShowErrorMessage(exception),
                    event,
                )
            }
        }
}
