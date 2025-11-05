package com.team.ui_compose.fixture

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
    )
