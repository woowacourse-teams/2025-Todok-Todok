package com.team.data.fixture

import com.team.domain.model.Book
import com.team.domain.model.Discussion
import com.team.domain.model.member.User
import java.time.LocalDateTime

val DISCUSSIONS_FIXTURE =
    listOf(
        Discussion(
            id = 1L,
            discussionTitle = "JPA 성능 최적화",
            book = Book(1L, "자바 ORM 표준 JPA 프로그래밍", "김영한", ""),
            writer = User(1L, "홍길동", ""),
            createAt = LocalDateTime.of(2025, 7, 12, 12, 0),
            discussionOpinion = "fetch join을 남발하면 안됩니다.",
            likeCount = 0,
            commentCount = 0,
            viewCount = 0,
            isLikedByMe = false,
        ),
        Discussion(
            id = 2L,
            discussionTitle = "코틀린 코루틴 완전 정복",
            book = Book(2L, "Kotlin in Action", "Dmitry Jemerov", ""),
            writer = User(2L, "박코루틴", ""),
            createAt = LocalDateTime.of(2025, 7, 13, 12, 0),
            discussionOpinion = "suspend fun과 launch 차이를 이해해야 합니다.",
            likeCount = 0,
            viewCount = 0,
            commentCount = 0,
            isLikedByMe = false,
        ),
        Discussion(
            id = 3L,
            discussionTitle = "MVVM 구조 제대로 이해하기",
            book = Book(3L, "안드로이드 아키텍처 가이드", "구글", ""),
            writer = User(3L, "김아키텍처", ""),
            createAt = LocalDateTime.of(2025, 7, 14, 12, 0),
            discussionOpinion = "UI와 로직을 분리해 유지보수가 쉬워집니다.",
            likeCount = 0,
            commentCount = 0,
            viewCount = 0,
            isLikedByMe = false,
        ),
        Discussion(
            id = 4L,
            discussionTitle = "클린 코드란 무엇인가?",
            book = Book(4L, "Clean Code", "Robert C. Martin", ""),
            writer = User(4L, "이클린", ""),
            createAt = LocalDateTime.of(2025, 7, 15, 12, 0),
            discussionOpinion = "의도를 드러내는 코드가 중요합니다.",
            likeCount = 0,
            commentCount = 0,
            viewCount = 0,
            isLikedByMe = false,
        ),
        Discussion(
            id = 5L,
            discussionTitle = "디자인 패턴 다시 보기",
            book = Book(5L, "Head First Design Patterns", "Eric Freeman", ""),
            writer = User(5L, "정디자인", ""),
            createAt = LocalDateTime.of(2025, 7, 16, 12, 0),
            discussionOpinion = "상황에 맞는 패턴 선택이 중요합니다.",
            likeCount = 0,
            commentCount = 0,
            viewCount = 0,
            isLikedByMe = false,
        ),
    )
