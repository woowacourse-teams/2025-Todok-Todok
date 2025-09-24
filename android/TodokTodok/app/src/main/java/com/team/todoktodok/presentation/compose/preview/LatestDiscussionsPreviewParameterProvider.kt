package com.team.todoktodok.presentation.compose.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.team.domain.model.Book
import com.team.domain.model.Discussion
import com.team.domain.model.member.Nickname
import com.team.domain.model.member.User
import com.team.todoktodok.presentation.compose.discussion.latest.LatestDiscussionsUiState
import com.team.todoktodok.presentation.xml.discussions.DiscussionUiState
import java.time.LocalDateTime

class LatestDiscussionsPreviewParameterProvider : PreviewParameterProvider<LatestDiscussionsUiState> {
    override val values: Sequence<LatestDiscussionsUiState>
        get() =
            sequenceOf(
                LatestDiscussionsUiState(
                    discussions =
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
                            DiscussionUiState(
                                item =
                                    Discussion(
                                        id = 9L,
                                        discussionTitle = "마이크로서비스 제대로 알기",
                                        book = Book(9L, "Building Microservices", "Sam Newman", ""),
                                        writer = User(9L, Nickname("김마이크로"), ""),
                                        createAt = LocalDateTime.of(2025, 7, 20, 12, 0),
                                        discussionOpinion = "서비스 간 독립성과 배포 유연성이 장점입니다.",
                                        likeCount = 20,
                                        commentCount = 10,
                                        isLikedByMe = true,
                                    ),
                            ),
                            DiscussionUiState(
                                item =
                                    Discussion(
                                        id = 10L,
                                        discussionTitle = "클라우드 네이티브 애플리케이션",
                                        book = Book(10L, "Cloud Native Patterns", "Cornelia Davis", ""),
                                        writer = User(10L, Nickname("이클라우드"), ""),
                                        createAt = LocalDateTime.of(2025, 7, 21, 12, 0),
                                        discussionOpinion = "컨테이너 기반 아키텍처가 필수입니다.",
                                        likeCount = 9,
                                        commentCount = 1,
                                        isLikedByMe = false,
                                    ),
                            ),
                            DiscussionUiState(
                                item =
                                    Discussion(
                                        id = 11L,
                                        discussionTitle = "함수형 프로그래밍 사고",
                                        book =
                                            Book(
                                                11L,
                                                "Functional Programming in Scala",
                                                "Paul Chiusano",
                                                "",
                                            ),
                                        writer = User(11L, Nickname("정함수"), ""),
                                        createAt = LocalDateTime.of(2025, 7, 22, 12, 0),
                                        discussionOpinion = "불변성과 순수함수 개념이 중요합니다.",
                                        likeCount = 6,
                                        commentCount = 2,
                                        isLikedByMe = true,
                                    ),
                            ),
                            DiscussionUiState(
                                item =
                                    Discussion(
                                        id = 12L,
                                        discussionTitle = "데이터베이스 샤딩 전략",
                                        book =
                                            Book(
                                                12L,
                                                "Designing Data-Intensive Applications",
                                                "Martin Kleppmann",
                                                "",
                                            ),
                                        writer = User(12L, Nickname("박데이터"), ""),
                                        createAt = LocalDateTime.of(2025, 7, 23, 12, 0),
                                        discussionOpinion = "대규모 트래픽에는 샤딩이 필수입니다.",
                                        likeCount = 18,
                                        commentCount = 8,
                                        isLikedByMe = false,
                                    ),
                            ),
                            DiscussionUiState(
                                item =
                                    Discussion(
                                        id = 13L,
                                        discussionTitle = "CI/CD 파이프라인 구축",
                                        book = Book(13L, "Continuous Delivery", "Jez Humble", ""),
                                        writer = User(13L, Nickname("윤파이프라인"), ""),
                                        createAt = LocalDateTime.of(2025, 7, 24, 12, 0),
                                        discussionOpinion = "자동화된 배포는 팀의 생산성을 높입니다.",
                                        likeCount = 22,
                                        commentCount = 5,
                                        isLikedByMe = true,
                                    ),
                            ),
                            DiscussionUiState(
                                item =
                                    Discussion(
                                        id = 14L,
                                        discussionTitle = "GraphQL vs REST",
                                        book = Book(14L, "Learning GraphQL", "Eve Porcello", ""),
                                        writer = User(14L, Nickname("최그래프"), ""),
                                        createAt = LocalDateTime.of(2025, 7, 25, 12, 0),
                                        discussionOpinion = "GraphQL은 필요한 데이터만 가져올 수 있어 효율적입니다.",
                                        likeCount = 13,
                                        commentCount = 7,
                                        isLikedByMe = false,
                                    ),
                            ),
                            DiscussionUiState(
                                item =
                                    Discussion(
                                        id = 15L,
                                        discussionTitle = "AI와 소프트웨어 개발",
                                        book = Book(15L, "AI Superpowers", "Kai-Fu Lee", ""),
                                        writer = User(15L, Nickname("홍AI"), ""),
                                        createAt = LocalDateTime.of(2025, 7, 26, 12, 0),
                                        discussionOpinion = "AI가 개발 생산성을 크게 바꿀 것입니다.",
                                        likeCount = 30,
                                        commentCount = 12,
                                        isLikedByMe = true,
                                    ),
                            ),
                        ),
                ),
            )
}
