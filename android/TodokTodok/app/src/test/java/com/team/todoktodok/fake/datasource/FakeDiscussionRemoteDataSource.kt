package com.team.todoktodok.fake.datasource

import com.team.domain.model.DiscussionFilter
import com.team.todoktodok.data.datasource.discussion.DiscussionRemoteDataSource
import com.team.todoktodok.data.network.model.LikeAction
import com.team.todoktodok.data.network.response.discussion.BookResponse
import com.team.todoktodok.data.network.response.discussion.DiscussionResponse
import com.team.todoktodok.data.network.response.discussion.MemberResponse
import retrofit2.Response

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
                likeCount = 0,
                commentCount = 0,
                isLikedByMe = false,
            ),
            DiscussionResponse(
                discussionId = 2,
                discussionTitle = "코틀린 코루틴 완전 정복",
                bookResponse = BookResponse("Dmitry Jemerov", 2, "", "Kotlin in Action"),
                discussionOpinion = "suspend fun과 launch 차이를 이해해야 합니다.",
                createdAt = "2025-07-13T12:00:00",
                memberResponse = MemberResponse(2, "박코루틴"),
                likeCount = 0,
                commentCount = 0,
                isLikedByMe = false,
            ),
            DiscussionResponse(
                discussionId = 3,
                discussionTitle = "MVVM 구조 제대로 이해하기",
                bookResponse = BookResponse("구글", 3, "", "안드로이드 아키텍처 가이드"),
                discussionOpinion = "UI와 로직을 분리해 유지보수가 쉬워집니다.",
                createdAt = "2025-07-14T12:00:00",
                memberResponse = MemberResponse(3, "김아키텍처"),
                likeCount = 0,
                commentCount = 0,
                isLikedByMe = false,
            ),
            DiscussionResponse(
                discussionId = 4,
                discussionTitle = "클린 코드란 무엇인가?",
                bookResponse = BookResponse("Robert C. Martin", 4, "", "Clean Code"),
                discussionOpinion = "의도를 드러내는 코드가 중요합니다.",
                createdAt = "2025-07-15T12:00:00",
                memberResponse = MemberResponse(4, "이클린"),
                likeCount = 0,
                commentCount = 0,
                isLikedByMe = false,
            ),
            DiscussionResponse(
                discussionId = 5,
                discussionTitle = "디자인 패턴 다시 보기",
                bookResponse = BookResponse("Eric Freeman", 5, "", "Head First Design Patterns"),
                discussionOpinion = "상황에 맞는 패턴 선택이 중요합니다.",
                createdAt = "2025-07-16T12:00:00",
                memberResponse = MemberResponse(5, "정디자인"),
                likeCount = 0,
                commentCount = 0,
                isLikedByMe = false,
            ),
        )

    override suspend fun getDiscussion(id: Long): Result<DiscussionResponse> =
        runCatching {
            discussionResponses.find { id == it.discussionId } ?: throw IllegalArgumentException()
        }

    override suspend fun getDiscussions(
        type: DiscussionFilter,
        keyword: String?,
    ): List<DiscussionResponse> =
        discussionResponses.filter { discussion ->
            val matchesKeyword =
                keyword.isNullOrBlank() ||
                    discussion.discussionTitle.contains(keyword, ignoreCase = true)

            val matchesType =
                when (type) {
                    DiscussionFilter.ALL -> true
                    DiscussionFilter.MINE -> discussion.memberResponse.memberId == 1L
                }

            matchesKeyword && matchesType
        }

    override suspend fun saveDiscussionRoom(
        bookId: Long,
        discussionTitle: String,
        discussionOpinion: String,
    ): Response<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun editDiscussionRoom(
        discussionId: Long,
        discussionTitle: String,
        discussionOpinion: String,
    ): Response<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteDiscussion(discussionId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun toggleLike(discussionId: Long): LikeAction {
        TODO("Not yet implemented")
    }

    override suspend fun reportDiscussion(discussionId: Long) {
        TODO("Not yet implemented")
    }
}
