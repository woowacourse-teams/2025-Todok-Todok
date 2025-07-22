package com.example.todoktodok.fixture

import com.example.domain.model.Comment
import java.time.LocalDateTime

val COMMENTS =
    listOf(
        Comment(1, "정말 좋은 글이에요!", USERS[0], LocalDateTime.of(2024, 3, 15, 10, 30)),
        Comment(2, "감사합니다. 도움이 됐어요.", USERS[1], LocalDateTime.of(2024, 4, 1, 14, 45)),
        Comment(3, "조금 더 설명이 필요할 것 같아요.", USERS[2], LocalDateTime.of(2024, 5, 5, 9, 10)),
        Comment(4, "저도 같은 생각이에요!", USERS[3], LocalDateTime.of(2024, 6, 10, 18, 0)),
        Comment(5, "수고 많으셨습니다.", USERS[4], LocalDateTime.of(2024, 7, 20, 22, 15)),
    )
