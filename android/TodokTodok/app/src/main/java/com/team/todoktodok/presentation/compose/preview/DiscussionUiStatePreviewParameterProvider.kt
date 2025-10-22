package com.team.todoktodok.presentation.compose.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.team.domain.model.Book
import com.team.domain.model.Discussion
import com.team.domain.model.member.User
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionUiModel
import java.time.LocalDateTime

class DiscussionUiStatePreviewParameterProvider : PreviewParameterProvider<List<DiscussionUiModel>> {
    override val values: Sequence<List<DiscussionUiModel>>
        get() =
            sequenceOf(
                listOf(
                    DiscussionUiModel(
                        item =
                            Discussion(
                                id = 1L,
                                discussionTitle = "JPA 성능 최적화",
                                book =
                                    Book(
                                        id = 1L,
                                        title = "자바 ORM 표준 JPA 프로그래밍",
                                        author = "김영한",
                                        image = "",
                                    ),
                                writer = User(1L, "홍길동", ""),
                                createAt = LocalDateTime.of(2025, 7, 12, 12, 0),
                                discussionOpinion =
                                    """
                                    응집도와 결합도의 차이에 대한 의견이 궁금합니다.
                                    응집도는 내부에 유사한 책임이 얼마나 모여 있는가를 의미합니다.
                                    """.trimIndent(),
                                likeCount = 0,
                                commentCount = 0,
                                viewCount = 0,
                                isLikedByMe = false,
                            ),
                    ),
                    DiscussionUiModel(
                        item =
                            Discussion(
                                id = 2L,
                                discussionTitle = "코틀린 코루틴 완전 정복",
                                book =
                                    Book(
                                        id = 2L,
                                        title = "Kotlin in Action",
                                        author = "Dmitry Jemerov",
                                        image = "",
                                    ),
                                writer = User(2L, "박코루틴", ""),
                                createAt = LocalDateTime.of(2025, 7, 13, 12, 0),
                                discussionOpinion = "suspend fun과 launch 차이를 이해해야 합니다.",
                                likeCount = 10,
                                viewCount = 0,
                                commentCount = 2,
                                isLikedByMe = true,
                            ),
                    ),
                    DiscussionUiModel(
                        item =
                            Discussion(
                                id = 3L,
                                discussionTitle = "MVVM 구조 제대로 이해하기",
                                book =
                                    Book(
                                        id = 3L,
                                        title = "안드로이드 아키텍처 가이드",
                                        author = "Google",
                                        image = "",
                                    ),
                                writer = User(3L, "김아키텍처", ""),
                                createAt = LocalDateTime.of(2025, 7, 14, 12, 0),
                                discussionOpinion = "UI와 로직을 분리해 유지보수가 쉬워집니다.",
                                likeCount = 3,
                                viewCount = 0,
                                commentCount = 1,
                                isLikedByMe = false,
                            ),
                    ),
                    DiscussionUiModel(
                        item =
                            Discussion(
                                id = 4L,
                                discussionTitle = "클린 코드란 무엇인가?",
                                book =
                                    Book(
                                        id = 4L,
                                        title = "Clean Code",
                                        author = "Robert C. Martin",
                                        image = "",
                                    ),
                                writer = User(4L, "이클린", ""),
                                createAt = LocalDateTime.of(2025, 7, 15, 12, 0),
                                discussionOpinion = "의도를 드러내는 코드가 중요합니다.",
                                likeCount = 5,
                                viewCount = 0,
                                commentCount = 4,
                                isLikedByMe = true,
                            ),
                    ),
                    DiscussionUiModel(
                        item =
                            Discussion(
                                id = 5L,
                                discussionTitle = "디자인 패턴 다시 보기",
                                book =
                                    Book(
                                        id = 5L,
                                        title = "Head First Design Patterns",
                                        author = "Eric Freeman",
                                        image = "",
                                    ),
                                writer = User(5L, "정디자인", ""),
                                createAt = LocalDateTime.of(2025, 7, 16, 12, 0),
                                discussionOpinion = "상황에 맞는 패턴 선택이 중요합니다.",
                                viewCount = 0,
                                likeCount = 8,
                                commentCount = 0,
                                isLikedByMe = false,
                            ),
                    ),
                    DiscussionUiModel(
                        item =
                            Discussion(
                                id = 6L,
                                discussionTitle = "DDD와 헥사고날 아키텍처",
                                book =
                                    Book(
                                        id = 6L,
                                        title = "Domain-Driven Design",
                                        author = "Eric Evans",
                                        image = "",
                                    ),
                                writer = User(6L, "유DDD", ""),
                                createAt = LocalDateTime.of(2025, 7, 17, 12, 0),
                                discussionOpinion = "도메인 모델을 중심으로 설계해야 합니다.",
                                viewCount = 0,
                                likeCount = 15,
                                commentCount = 6,
                                isLikedByMe = true,
                            ),
                    ),
                    DiscussionUiModel(
                        item =
                            Discussion(
                                id = 7L,
                                discussionTitle = "테스트 주도 개발의 힘",
                                book =
                                    Book(
                                        id = 7L,
                                        title = "Test Driven Development",
                                        author = "Kent Beck",
                                        image = "",
                                    ),
                                writer = User(7L, "박테스트", ""),
                                createAt = LocalDateTime.of(2025, 7, 18, 12, 0),
                                discussionOpinion = "작은 단위의 테스트가 코드 품질을 지켜줍니다.",
                                viewCount = 0,
                                likeCount = 12,
                                commentCount = 3,
                                isLikedByMe = false,
                            ),
                    ),
                    DiscussionUiModel(
                        item =
                            Discussion(
                                id = 8L,
                                discussionTitle = "리액티브 프로그래밍의 이해",
                                book =
                                    Book(
                                        id = 8L,
                                        title = "Reactive Programming with RxJava",
                                        author = "Tomasz Nurkiewicz",
                                        image = "",
                                    ),
                                writer = User(8L, "최리액티브", ""),
                                createAt = LocalDateTime.of(2025, 7, 19, 12, 0),
                                discussionOpinion = "데이터 스트림과 비동기를 효과적으로 처리할 수 있습니다.",
                                viewCount = 0,
                                likeCount = 7,
                                commentCount = 2,
                                isLikedByMe = false,
                            ),
                    ),
                ),
            )
}
