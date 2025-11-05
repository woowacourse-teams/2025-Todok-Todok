package com.team.data.fixture

import com.team.data.network.response.comment.CommentResponse

val COMMENT_RESPONSES_FIXTURE =
    listOf(
        CommentResponse(
            commentId = 1,
            content = "정말 좋은 글이에요!",
            createdAt = "2024-03-15T10:30:00",
            member = MEMBERS_RESPONSE_FIXTURE[0],
            likeCount = 0,
            replyCount = 0,
            isLikedByMe = false,
        ),
        CommentResponse(
            commentId = 2,
            content = "감사합니다. 도움이 됐어요.",
            createdAt = "2024-04-01T14:45:00",
            member = MEMBERS_RESPONSE_FIXTURE[1],
            likeCount = 0,
            replyCount = 0,
            isLikedByMe = false,
        ),
        CommentResponse(
            commentId = 3,
            content = "조금 더 설명이 필요할 것 같아요.",
            createdAt = "2024-05-05T09:10:00",
            member = MEMBERS_RESPONSE_FIXTURE[2],
            likeCount = 0,
            replyCount = 0,
            isLikedByMe = false,
        ),
        CommentResponse(
            commentId = 4,
            content = "저도 같은 생각이에요!",
            createdAt = "2024-06-10T18:00:00",
            member = MEMBERS_RESPONSE_FIXTURE[3],
            likeCount = 0,
            replyCount = 0,
            isLikedByMe = false,
        ),
        CommentResponse(
            commentId = 5,
            content = "수고 많으셨습니다.",
            createdAt = "2024-07-20T22:15:00",
            member = MEMBERS_RESPONSE_FIXTURE[4],
            likeCount = 0,
            replyCount = 0,
            isLikedByMe = false,
        ),
    )
