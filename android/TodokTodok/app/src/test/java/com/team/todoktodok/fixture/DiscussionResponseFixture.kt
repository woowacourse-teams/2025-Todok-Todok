package com.team.todoktodok.fixture

import com.team.todoktodok.data.network.response.discussion.BookResponse
import com.team.todoktodok.data.network.response.discussion.DiscussionResponse
import com.team.todoktodok.data.network.response.discussion.MemberResponse

val DISCUSSION_RESPONSE =
    listOf(
        DiscussionResponse(
            discussionId = 1,
            discussionTitle = "JPA 성능 최적화",
            book = BookResponse("김영한", 1, "", "자바 ORM 표준 JPA 프로그래밍"),
            discussionOpinion = "fetch join을 남발하면 안됩니다.",
            createdAt = "2025-07-12T12:00:00",
            member = MemberResponse(1, "홍길동"),
            likeCount = 0,
            commentCount = 0,
            isLikedByMe = false,
        ),
        DiscussionResponse(
            discussionId = 2,
            discussionTitle = "코틀린 코루틴 완전 정복",
            book = BookResponse("Dmitry Jemerov", 2, "", "Kotlin in Action"),
            discussionOpinion = "suspend fun과 launch 차이를 이해해야 합니다.",
            createdAt = "2025-07-13T12:00:00",
            member = MemberResponse(2, "박코루틴"),
            likeCount = 0,
            commentCount = 0,
            isLikedByMe = false,
        ),
        DiscussionResponse(
            discussionId = 3,
            discussionTitle = "MVVM 구조 제대로 이해하기",
            book = BookResponse("구글", 3, "", "안드로이드 아키텍처 가이드"),
            discussionOpinion = "UI와 로직을 분리해 유지보수가 쉬워집니다.",
            createdAt = "2025-07-14T12:00:00",
            member = MemberResponse(3, "김아키텍처"),
            likeCount = 0,
            commentCount = 0,
            isLikedByMe = false,
        ),
        DiscussionResponse(
            discussionId = 4,
            discussionTitle = "클린 코드란 무엇인가?",
            book = BookResponse("Robert C. Martin", 4, "", "Clean Code"),
            discussionOpinion = "의도를 드러내는 코드가 중요합니다.",
            createdAt = "2025-07-15T12:00:00",
            member = MemberResponse(4, "이클린"),
            likeCount = 0,
            commentCount = 0,
            isLikedByMe = false,
        ),
        DiscussionResponse(
            discussionId = 5,
            discussionTitle = "디자인 패턴 다시 보기",
            book = BookResponse("Eric Freeman", 5, "", "Head First Design Patterns"),
            discussionOpinion = "상황에 맞는 패턴 선택이 중요합니다.",
            createdAt = "2025-07-16T12:00:00",
            member = MemberResponse(5, "정디자인"),
            likeCount = 0,
            commentCount = 0,
            isLikedByMe = false,
        ),
    )
