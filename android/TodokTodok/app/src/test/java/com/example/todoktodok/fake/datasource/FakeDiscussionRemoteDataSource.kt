package com.example.todoktodok.fake.datasource

import com.example.todoktodok.data.datasource.discussion.DiscussionRemoteDataSource
import com.example.todoktodok.data.network.response.discussion.BookResponse
import com.example.todoktodok.data.network.response.discussion.DiscussionResponse
import com.example.todoktodok.data.network.response.discussion.MemberResponse
import com.example.todoktodok.data.network.response.discussion.NoteResponse

class FakeDiscussionRemoteDataSource : DiscussionRemoteDataSource {
    val discussionResponses =
        listOf(
            DiscussionResponse(
                discussionId = 1,
                discussionTitle = "JPA 성능 최적화",
                bookResponse = BookResponse("김영한", 1, "", "자바 ORM 표준 JPA 프로그래밍"),
                discussionOpinion = "fetch join을 남발하면 안됩니다.",
                createdAt = "2025-07-12T12:00:00",
                memberResponse = MemberResponse(1, "홍길동"),
                noteResponse =
                    NoteResponse(
                        noteId = 101,
                        memo = "엔티티 직접 반환 주의",
                        snap = "노트 스냅샷 1",
                        book = BookResponse("김영한", 1, "", "자바 ORM 표준 JPA 프로그래밍"),
                    ),
            ),
            DiscussionResponse(
                discussionId = 2,
                discussionTitle = "코틀린 코루틴 완전 정복",
                bookResponse = BookResponse("Dmitry Jemerov", 2, "", "Kotlin in Action"),
                discussionOpinion = "suspend fun과 launch 차이를 이해해야 합니다.",
                createdAt = "2025-07-13T12:00:00",
                memberResponse = MemberResponse(2, "박코루틴"),
                noteResponse =
                    NoteResponse(
                        noteId = 102,
                        memo = "코루틴 컨텍스트 정리 필요",
                        snap = "노트 스냅샷 2",
                        book = BookResponse("Dmitry Jemerov", 2, "", "Kotlin in Action"),
                    ),
            ),
            DiscussionResponse(
                discussionId = 3,
                discussionTitle = "MVVM 구조 제대로 이해하기",
                bookResponse = BookResponse("구글", 3, "", "안드로이드 아키텍처 가이드"),
                discussionOpinion = "UI와 로직을 분리해 유지보수가 쉬워집니다.",
                createdAt = "2025-07-14T12:00:00",
                memberResponse = MemberResponse(3, "김아키텍처"),
                noteResponse =
                    NoteResponse(
                        noteId = 103,
                        memo = "MVVM 계층 구조 정리",
                        snap = "노트 스냅샷 3",
                        book = BookResponse("구글", 3, "", "안드로이드 아키텍처 가이드"),
                    ),
            ),
            DiscussionResponse(
                discussionId = 4,
                discussionTitle = "클린 코드란 무엇인가?",
                bookResponse = BookResponse("Robert C. Martin", 4, "", "Clean Code"),
                discussionOpinion = "의도를 드러내는 코드가 중요합니다.",
                createdAt = "2025-07-15T12:00:00",
                memberResponse = MemberResponse(4, "이클린"),
                noteResponse =
                    NoteResponse(
                        noteId = 104,
                        memo = "클린 코드 원칙 요약",
                        snap = "노트 스냅샷 4",
                        book = BookResponse("Robert C. Martin", 4, "", "Clean Code"),
                    ),
            ),
            DiscussionResponse(
                discussionId = 5,
                discussionTitle = "디자인 패턴 다시 보기",
                bookResponse = BookResponse("Eric Freeman", 5, "", "Head First Design Patterns"),
                discussionOpinion = "상황에 맞는 패턴 선택이 중요합니다.",
                createdAt = "2025-07-16T12:00:00",
                memberResponse = MemberResponse(5, "정디자인"),
                noteResponse =
                    NoteResponse(
                        noteId = 105,
                        memo = "디자인 패턴 종류 정리",
                        snap = "노트 스냅샷 5",
                        book = BookResponse("Eric Freeman", 5, "", "Head First Design Patterns"),
                    ),
            ),
        )

    override suspend fun getDiscussion(id: Long): Result<DiscussionResponse> =
        runCatching {
            discussionResponses.find { id == it.discussionId } ?: throw IllegalArgumentException()
        }

    override suspend fun getDiscussions(): List<DiscussionResponse> = discussionResponses
}
