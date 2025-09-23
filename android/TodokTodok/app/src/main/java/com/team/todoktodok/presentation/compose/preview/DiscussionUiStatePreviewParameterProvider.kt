package com.team.todoktodok.presentation.compose.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.team.domain.model.Book
import com.team.domain.model.Discussion
import com.team.domain.model.member.Nickname
import com.team.domain.model.member.User
import com.team.todoktodok.presentation.xml.discussions.DiscussionUiState
import java.time.LocalDateTime

class DiscussionUiStatePreviewParameterProvider : PreviewParameterProvider<List<DiscussionUiState>> {
    override val values: Sequence<List<DiscussionUiState>>
        get() =
            sequenceOf(
                listOf(
                    DiscussionUiState(
                        item =
                            Discussion(
                                id = 1L,
                                discussionTitle = "JPA 성능 최적화",
                                book = Book(1L, "자바 ORM 표준 JPA 프로그래밍", "김영한", ""),
                                writer = User(1L, Nickname("홍길동"), ""),
                                createAt = LocalDateTime.of(2025, 7, 12, 12, 0),
                                discussionOpinion = "fetch join을 남발하면 안됩니다.",
                                likeCount = 0,
                                commentCount = 0,
                                isLikedByMe = false,
                            ),
                        searchKeyword = "JPA",
                    ),
                    DiscussionUiState(
                        item =
                            Discussion(
                                id = 2L,
                                discussionTitle = "코틀린 코루틴 완전 정복",
                                book = Book(2L, "Kotlin in Action", "Dmitry Jemerov", ""),
                                writer = User(2L, Nickname("박코루틴"), ""),
                                createAt = LocalDateTime.of(2025, 7, 13, 12, 0),
                                discussionOpinion = "suspend fun과 launch 차이를 이해해야 합니다.",
                                likeCount = 10,
                                commentCount = 2,
                                isLikedByMe = true,
                            ),
                    ),
                    DiscussionUiState(
                        item =
                            Discussion(
                                id = 3L,
                                discussionTitle = "MVVM 구조 제대로 이해하기",
                                book = Book(3L, "안드로이드 아키텍처 가이드", "구글", ""),
                                writer = User(3L, Nickname("김아키텍처"), ""),
                                createAt = LocalDateTime.of(2025, 7, 14, 12, 0),
                                discussionOpinion = "UI와 로직을 분리해 유지보수가 쉬워집니다.",
                                likeCount = 3,
                                commentCount = 1,
                                isLikedByMe = false,
                            ),
                    ),
                    DiscussionUiState(
                        item =
                            Discussion(
                                id = 4L,
                                discussionTitle = "클린 코드란 무엇인가?",
                                book = Book(4L, "Clean Code", "Robert C. Martin", ""),
                                writer = User(4L, Nickname("이클린"), ""),
                                createAt = LocalDateTime.of(2025, 7, 15, 12, 0),
                                discussionOpinion = "의도를 드러내는 코드가 중요합니다.",
                                likeCount = 5,
                                commentCount = 4,
                                isLikedByMe = true,
                            ),
                    ),
                    DiscussionUiState(
                        item =
                            Discussion(
                                id = 5L,
                                discussionTitle = "디자인 패턴 다시 보기",
                                book =
                                    Book(
                                        5L,
                                        "Head First Design Patterns",
                                        "Eric Freeman",
                                        "",
                                    ),
                                writer = User(5L, Nickname("정디자인"), ""),
                                createAt = LocalDateTime.of(2025, 7, 16, 12, 0),
                                discussionOpinion = "상황에 맞는 패턴 선택이 중요합니다.",
                                likeCount = 8,
                                commentCount = 0,
                                isLikedByMe = false,
                            ),
                    ),
                    DiscussionUiState(
                        item =
                            Discussion(
                                id = 6L,
                                discussionTitle = "DDD와 헥사고날 아키텍처",
                                book = Book(6L, "Domain-Driven Design", "Eric Evans", ""),
                                writer = User(6L, Nickname("유DDD"), ""),
                                createAt = LocalDateTime.of(2025, 7, 17, 12, 0),
                                discussionOpinion = "도메인 모델을 중심으로 설계해야 합니다.",
                                likeCount = 15,
                                commentCount = 6,
                                isLikedByMe = true,
                            ),
                    ),
                    DiscussionUiState(
                        item =
                            Discussion(
                                id = 7L,
                                discussionTitle = "테스트 주도 개발의 힘",
                                book = Book(7L, "Test Driven Development", "Kent Beck", ""),
                                writer = User(7L, Nickname("박테스트"), ""),
                                createAt = LocalDateTime.of(2025, 7, 18, 12, 0),
                                discussionOpinion = "작은 단위의 테스트가 코드 품질을 지켜줍니다.",
                                likeCount = 12,
                                commentCount = 3,
                                isLikedByMe = false,
                            ),
                    ),
                    DiscussionUiState(
                        item =
                            Discussion(
                                id = 8L,
                                discussionTitle = "리액티브 프로그래밍의 이해",
                                book =
                                    Book(
                                        8L,
                                        "Reactive Programming with RxJava",
                                        "Tomasz Nurkiewicz",
                                        "",
                                    ),
                                writer = User(8L, Nickname("최리액티브"), ""),
                                createAt = LocalDateTime.of(2025, 7, 19, 12, 0),
                                discussionOpinion = "데이터 스트림과 비동기를 효과적으로 처리할 수 있습니다.",
                                likeCount = 7,
                                commentCount = 2,
                                isLikedByMe = false,
                            ),
                    ),
                ),
            )
}
