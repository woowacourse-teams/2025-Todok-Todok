package com.example.todoktodok.data.datasource

import com.example.domain.model.Comment
import com.example.domain.model.member.Nickname
import com.example.domain.model.member.User
import java.time.LocalDateTime

class DefaultCommentDataSource : CommentDataSource {
    private val users =
        listOf(
            User(1, Nickname("Alice")),
            User(2, Nickname("Bob")),
            User(3, Nickname("Charlie")),
            User(4, Nickname("Diana")),
            User(5, Nickname("Ethan")),
        )

    private val dummyComments =
        listOf(
            Comment(1, "정말 좋은 글이에요!", users[0], LocalDateTime.of(2024, 3, 15, 10, 30)),
            Comment(2, "감사합니다. 도움이 됐어요.", users[1], LocalDateTime.of(2024, 4, 1, 14, 45)),
            Comment(3, "조금 더 설명이 필요할 것 같아요.", users[2], LocalDateTime.of(2024, 5, 5, 9, 10)),
            Comment(4, "저도 같은 생각이에요!", users[3], LocalDateTime.of(2024, 6, 10, 18, 0)),
            Comment(5, "수고 많으셨습니다.", users[4], LocalDateTime.of(2024, 7, 20, 22, 15)),
        )

    override fun getCommentsByDiscussionRoomId(id: Long): List<Comment> = dummyComments
}
