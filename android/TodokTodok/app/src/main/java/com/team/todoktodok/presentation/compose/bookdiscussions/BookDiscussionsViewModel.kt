package com.team.todoktodok.presentation.compose.bookdiscussions

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.team.domain.model.Book
import com.team.domain.model.Discussion
import com.team.domain.model.member.Nickname
import com.team.domain.model.member.User
import com.team.domain.repository.BookRepository
import com.team.domain.repository.DiscussionRepository
import com.team.todoktodok.presentation.compose.bookdiscussions.model.BookDiscussionsUiState
import com.team.todoktodok.presentation.compose.bookdiscussions.model.toBookDetailUiState
import com.team.todoktodok.presentation.compose.bookdiscussions.model.toDiscussionItem
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime

class BookDiscussionsViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val bookRepository: BookRepository,
    private val discussionRepository: DiscussionRepository,
) : ViewModel() {
    private val route: BookDetailRoute = savedStateHandle.toRoute()
    private val bookId: Long = route.bookId
    private val _uiState: MutableStateFlow<BookDiscussionsUiState> =
        MutableStateFlow(bookDiscussionsUiState(bookId, bookRepository, discussionRepository))
    val uiState: StateFlow<BookDiscussionsUiState> = _uiState
}

private fun bookDiscussionsUiState(
    bookId: Long,
    bookRepository: BookRepository,
    discussionRepository: DiscussionRepository,
): BookDiscussionsUiState =
    BookDiscussionsUiState.Success(
        Book(
            0,
            "오브젝트-코드로 이해하는 객체지향 설계",
            "조영호",
            "",
            "테스트",
            "테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트",
        ).toBookDetailUiState(),
        listOf(
            Discussion(
                id = 1L,
                discussionTitle = "JPA 성능 최적화",
                book =
                    Book(
                        1L,
                        "자바 ORM 표준 JPA 프로그래밍",
                        "김영한",
                        "",
                        "테스트",
                        "테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트",
                    ),
                writer = User(1L, Nickname("홍길동"), ""),
                createAt = LocalDateTime.of(2025, 7, 12, 12, 0),
                discussionOpinion =
                    "응집도와 결합도가 어떤 차이를 가지는 지에 대한 다른 분들의 생각이 궁금합니다." +
                        "응집도는 내부에 얼마나 비슷한 책임들이 모여있는가. 얼마나 연관있는 멤버들이 똘똘 뭉쳐있는가",
                likeCount = 12,
                commentCount = 3,
                viewCount = 10,
                isLikedByMe = false,
            ),
        ).map { it.toDiscussionItem() }.toImmutableList(),
    )
