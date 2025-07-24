package com.example.todoktodok.fake

import com.example.todoktodok.data.datasource.discussion.DiscussionRemoteDataSource
import com.example.todoktodok.data.network.response.discussion.BookResponse
import com.example.todoktodok.data.network.response.discussion.DiscussionResponse
import com.example.todoktodok.data.network.response.discussion.MemberResponse
import com.example.todoktodok.data.network.response.discussion.NoteResponse

class FakeDiscussionRemoteDataSource : DiscussionRemoteDataSource {
    private val discussionResponses =
        listOf(
            DiscussionResponse(
                discussionId = 1,
                discussionTitle = "JPA 성능 최적화",
                bookResponse = BookResponse("김영한", 1, "", "자바 ORM 표준 JPA 프로그래밍"),
                discussionOpinion = "fetch join을 남발하면 안됩니다.",
                createdAt = "2025-07-12T12:00:00Z",
                memberResponse = MemberResponse(1, "홍길동"),
                noteResponse =
                    NoteResponse(
                        noteId = 101,
                        memo = "엔티티 직접 반환 주의",
                        snap = "스냅샷 1",
                        book = BookResponse("김영한", 1, "", "자바 ORM 표준 JPA 프로그래밍"),
                    ),
            ),
            DiscussionResponse(
                discussionId = 2,
                discussionTitle = "트랜잭션 전파 전략",
                bookResponse = BookResponse("최범균", 2, "", "DDD START"),
                discussionOpinion = "REQUIRES_NEW는 신중히 써야 합니다.",
                createdAt = "2025-07-13T09:30:00Z",
                memberResponse = MemberResponse(2, "이순신"),
                noteResponse =
                    NoteResponse(
                        noteId = 102,
                        memo = "트랜잭션 격리 수준 정리 필요",
                        snap = "스냅샷 2",
                        book = BookResponse("최범균", 2, "", "DDD START"),
                    ),
            ),
            DiscussionResponse(
                discussionId = 3,
                discussionTitle = "지연 로딩과 N+1 문제",
                bookResponse = BookResponse("로버트 C. 마틴", 3, "", "클린 아키텍처"),
                discussionOpinion = "지연 로딩을 무작정 쓰면 위험합니다.",
                createdAt = "2025-07-14T14:45:00Z",
                memberResponse = MemberResponse(3, "강감찬"),
                noteResponse =
                    NoteResponse(
                        noteId = 103,
                        memo = "N+1 문제 발생 케이스 정리",
                        snap = "스냅샷 3",
                        book = BookResponse("로버트 C. 마틴", 3, "", "클린 아키텍처"),
                    ),
            ),
            DiscussionResponse(
                discussionId = 4,
                discussionTitle = "엔티티 vs DTO 분리",
                bookResponse = BookResponse("조슈아 블로크", 4, "", "Effective Java"),
                discussionOpinion = "서비스 계층에서는 DTO만 써야 합니다.",
                createdAt = "2025-07-15T16:20:00Z",
                memberResponse = MemberResponse(4, "장보고"),
                noteResponse =
                    NoteResponse(
                        noteId = 104,
                        memo = "엔티티 노출 지양 이유",
                        snap = "스냅샷 4",
                        book = BookResponse("조슈아 블로크", 4, "", "Effective Java"),
                    ),
            ),
            DiscussionResponse(
                discussionId = 5,
                discussionTitle = "Cascade 옵션 사용법",
                bookResponse = BookResponse("이일민", 5, "", "토비의 스프링"),
                discussionOpinion = "ALL보다는 필요한 옵션만 명시적으로 써야 합니다.",
                createdAt = "2025-07-16T10:10:00Z",
                memberResponse = MemberResponse(5, "유관순"),
                noteResponse =
                    NoteResponse(
                        noteId = 105,
                        memo = "cascade 종류 요약",
                        snap = "스냅샷 5",
                        book = BookResponse("이일민", 5, "", "토비의 스프링"),
                    ),
            ),
        )

    override suspend fun getDiscussion(id: Long): Result<DiscussionResponse> =
        runCatching {
            discussionResponses.find { id == it.discussionId } ?: throw IllegalArgumentException()
        }

    override suspend fun getDiscussions(): List<DiscussionResponse> = discussionResponses
}
