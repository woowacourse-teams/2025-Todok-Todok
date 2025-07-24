package com.example.todoktodok.fixture

import com.example.domain.model.Book
import com.example.domain.model.Discussion
import com.example.domain.model.member.Nickname
import com.example.domain.model.member.User
import com.example.todoktodok.data.network.response.discussion.BookResponse
import com.example.todoktodok.data.network.response.discussion.DiscussionResponse
import com.example.todoktodok.data.network.response.discussion.MemberResponse
import com.example.todoktodok.data.network.response.discussion.NoteResponse
import java.time.LocalDateTime

val DISCUSSIONS =
    listOf(
        Discussion(
            id = 1L,
            discussionTitle = "JPA 성능 최적화",
            book = Book(1L, "자바 ORM 표준 JPA 프로그래밍", "김영한", ""),
            writer = User(1L, Nickname("홍길동")),
            createAt = LocalDateTime.of(2025, 7, 12, 12, 0),
            snap = "노트 스냅샷 1",
            discussionOpinion = "fetch join을 남발하면 안됩니다.",
        ),
        Discussion(
            id = 2L,
            discussionTitle = "코틀린 코루틴 완전 정복",
            book = Book(2L, "Kotlin in Action", "Dmitry Jemerov", ""),
            writer = User(2L, Nickname("박코루틴")),
            createAt = LocalDateTime.of(2025, 7, 13, 12, 0),
            snap = "노트 스냅샷 2",
            discussionOpinion = "suspend fun과 launch 차이를 이해해야 합니다.",
        ),
        Discussion(
            id = 3L,
            discussionTitle = "MVVM 구조 제대로 이해하기",
            book = Book(3L, "안드로이드 아키텍처 가이드", "구글", ""),
            writer = User(3L, Nickname("김아키텍처")),
            createAt = LocalDateTime.of(2025, 7, 14, 12, 0),
            snap = "노트 스냅샷 3",
            discussionOpinion = "UI와 로직을 분리해 유지보수가 쉬워집니다.",
        ),
        Discussion(
            id = 4L,
            discussionTitle = "클린 코드란 무엇인가?",
            book = Book(4L, "Clean Code", "Robert C. Martin", ""),
            writer = User(4L, Nickname("이클린")),
            createAt = LocalDateTime.of(2025, 7, 15, 12, 0),
            snap = "노트 스냅샷 4",
            discussionOpinion = "의도를 드러내는 코드가 중요합니다.",
        ),
        Discussion(
            id = 5L,
            discussionTitle = "디자인 패턴 다시 보기",
            book = Book(5L, "Head First Design Patterns", "Eric Freeman", ""),
            writer = User(5L, Nickname("정디자인")),
            createAt = LocalDateTime.of(2025, 7, 16, 12, 0),
            snap = "노트 스냅샷 5",
            discussionOpinion = "상황에 맞는 패턴 선택이 중요합니다.",
        ),
    )

private val DISCUSSION_RESPONSES =
    listOf(
        DiscussionResponse(
            discussionId = 1,
            discussionTitle = "JPA 성능 최적화",
            bookResponse = BookResponse("김영한", 1, "자바 ORM 표준 JPA 프로그래밍", ""),
            discussionOpinion = "fetch join을 남발하면 안됩니다.",
            createdAt = "2025-07-12T12:00:00Z",
            memberResponse = MemberResponse(1, "홍길동"),
            noteResponse =
                NoteResponse(
                    noteId = 100,
                    memo = "이것은 노트 메모 1번입니다.",
                    snap = "노트 스냅샷 1",
                    book = BookResponse("김영한", 1, "자바 ORM 표준 JPA 프로그래밍", ""),
                ),
        ),
        DiscussionResponse(
            discussionId = 2,
            discussionTitle = "코틀린 코루틴 완전 정복",
            bookResponse = BookResponse("Dmitry Jemerov", 2, "Kotlin in Action", ""),
            discussionOpinion = "suspend fun과 launch 차이를 이해해야 합니다.",
            createdAt = "2025-07-13T12:00:00Z",
            memberResponse = MemberResponse(2, "박코루틴"),
            noteResponse =
                NoteResponse(
                    noteId = 101,
                    memo = "이것은 노트 메모 2번입니다.",
                    snap = "노트 스냅샷 2",
                    book = BookResponse("Dmitry Jemerov", 2, "Kotlin in Action", ""),
                ),
        ),
        DiscussionResponse(
            discussionId = 3,
            discussionTitle = "MVVM 구조 제대로 이해하기",
            bookResponse = BookResponse("구글", 3, "안드로이드 아키텍처 가이드", ""),
            discussionOpinion = "UI와 로직을 분리해 유지보수가 쉬워집니다.",
            createdAt = "2025-07-14T12:00:00Z",
            memberResponse = MemberResponse(3, "김아키텍처"),
            noteResponse =
                NoteResponse(
                    noteId = 102,
                    memo = "이것은 노트 메모 3번입니다.",
                    snap = "노트 스냅샷 3",
                    book = BookResponse("구글", 3, "안드로이드 아키텍처 가이드", ""),
                ),
        ),
        DiscussionResponse(
            discussionId = 4,
            discussionTitle = "클린 코드란 무엇인가?",
            bookResponse = BookResponse("Robert C. Martin", 4, "Clean Code", ""),
            discussionOpinion = "의도를 드러내는 코드가 중요합니다.",
            createdAt = "2025-07-15T12:00:00Z",
            memberResponse = MemberResponse(4, "이클린"),
            noteResponse =
                NoteResponse(
                    noteId = 103,
                    memo = "이것은 노트 메모 4번입니다.",
                    snap = "노트 스냅샷 4",
                    book = BookResponse("Robert C. Martin", 4, "Clean Code", ""),
                ),
        ),
        DiscussionResponse(
            discussionId = 5,
            discussionTitle = "디자인 패턴 다시 보기",
            bookResponse = BookResponse("Eric Freeman", 5, "Head First Design Patterns", ""),
            discussionOpinion = "상황에 맞는 패턴 선택이 중요합니다.",
            createdAt = "2025-07-16T12:00:00Z",
            memberResponse = MemberResponse(5, "정디자인"),
            noteResponse =
                NoteResponse(
                    noteId = 104,
                    memo = "이것은 노트 메모 5번입니다.",
                    snap = "노트 스냅샷 5",
                    book = BookResponse("Eric Freeman", 5, "Head First Design Patterns", ""),
                ),
        ),
    )
