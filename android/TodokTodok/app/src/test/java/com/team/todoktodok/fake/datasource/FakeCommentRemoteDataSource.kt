package com.team.todoktodok.fake.datasource

import com.team.domain.model.exception.NetworkResult
import com.team.todoktodok.data.datasource.comment.CommentRemoteDataSource
import com.team.todoktodok.data.network.model.LikeAction
import com.team.todoktodok.data.network.request.CommentRequest
import com.team.todoktodok.data.network.response.comment.CommentResponse
import com.team.todoktodok.data.network.response.discussion.MemberResponse
import java.time.LocalDateTime

class FakeCommentRemoteDataSource : CommentRemoteDataSource {
    private var like = LikeAction.UNLIKE
    private val dummyCommentResponses =
        mutableListOf(
            CommentResponse(
                commentId = 1L,
                content = "정말 좋은 글이에요!",
                createdAt = "2024-03-15T10:30:00.000",
                member =
                    MemberResponse(
                        memberId = 1L,
                        nickname = "Alice",
                        "",
                    ),
                likeCount = 0,
                replyCount = 0,
                isLikedByMe = false,
            ),
            CommentResponse(
                commentId = 2L,
                content = "감사합니다. 도움이 됐어요.",
                createdAt = "2024-04-01T14:45:00.000",
                member =
                    MemberResponse(
                        memberId = 2L,
                        nickname = "Bob",
                        "",
                    ),
                likeCount = 0,
                replyCount = 0,
                isLikedByMe = false,
            ),
            CommentResponse(
                commentId = 3L,
                content = "조금 더 설명이 필요할 것 같아요.",
                createdAt = "2024-05-05T09:10:00.000",
                member =
                    MemberResponse(
                        memberId = 3L,
                        nickname = "Charlie",
                        "",
                    ),
                likeCount = 0,
                replyCount = 0,
                isLikedByMe = false,
            ),
            CommentResponse(
                commentId = 4L,
                content = "저도 같은 생각이에요!",
                createdAt = "2024-06-10T18:00:00.000",
                member =
                    MemberResponse(
                        memberId = 4L,
                        nickname = "Diana",
                        "",
                    ),
                likeCount = 0,
                replyCount = 0,
                isLikedByMe = false,
            ),
            CommentResponse(
                commentId = 5L,
                content = "수고 많으셨습니다.",
                createdAt = "2024-07-20T22:15:00.000",
                member =
                    MemberResponse(
                        memberId = 5L,
                        nickname = "Ethan",
                        "",
                    ),
                likeCount = 0,
                replyCount = 0,
                isLikedByMe = false,
            ),
        )

    override suspend fun fetchComment(
        discussionId: Long,
        commentId: Long,
    ): NetworkResult<CommentResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchCommentsByDiscussionId(id: Long): NetworkResult<List<CommentResponse>> =
        NetworkResult.Success(dummyCommentResponses.toList())

    override suspend fun saveComment(
        discussionId: Long,
        comment: CommentRequest,
    ): NetworkResult<Unit> {
        dummyCommentResponses.add(
            CommentResponse(
                100,
                comment.content,
                LocalDateTime.of(2000, 7, 3, 10, 21).toString(),
                MemberResponse(1, "동전", ""),
                likeCount = 0,
                replyCount = 0,
                isLikedByMe = false,
            ),
        )
        return NetworkResult.Success(Unit)
    }

    override suspend fun toggleLike(
        discussionId: Long,
        commentId: Long,
    ): NetworkResult<LikeAction> =
        NetworkResult.Success(
            when (like) {
                LikeAction.LIKE -> LikeAction.UNLIKE
                LikeAction.UNLIKE -> LikeAction.LIKE
            },
        )

    override suspend fun updateComment(
        discussionId: Long,
        commentId: Long,
        comment: String,
    ): NetworkResult<Unit> {
        val index = dummyCommentResponses.indexOfFirst { it.commentId == commentId }
        if (index != -1) {
            dummyCommentResponses[index] = (
                CommentResponse(
                    100,
                    comment,
                    LocalDateTime.of(2000, 7, 3, 10, 21).toString(),
                    MemberResponse(1, "동전", ""),
                    likeCount = 0,
                    replyCount = 0,
                    isLikedByMe = false,
                )
            )
        }
        return NetworkResult.Success(Unit)
    }

    override suspend fun deleteComment(
        discussionId: Long,
        commentId: Long,
    ): NetworkResult<Unit> {
        dummyCommentResponses.removeIf { it.commentId == commentId }
        return NetworkResult.Success(Unit)
    }

    override suspend fun report(
        discussionId: Long,
        commentId: Long,
    ): NetworkResult<Unit> {
        TODO()
    }

    companion object {
        val fixture =
            CommentResponse(
                100,
                "하하",
                LocalDateTime.of(2000, 7, 3, 10, 21).toString(),
                MemberResponse(1, "동전", ""),
                likeCount = 0,
                replyCount = 0,
                isLikedByMe = false,
            )
    }
}
