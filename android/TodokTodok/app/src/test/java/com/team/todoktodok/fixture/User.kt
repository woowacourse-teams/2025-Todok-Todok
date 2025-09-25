package com.team.todoktodok.fixture

import com.team.domain.model.member.Nickname
import com.team.domain.model.member.User
import com.team.todoktodok.data.network.response.discussion.MemberResponse

val USERS =
    listOf(
        User(1, Nickname("Alice"), ""),
        User(2, Nickname("Bob"), ""),
        User(3, Nickname("Charlie"), ""),
        User(4, Nickname("Diana"), ""),
        User(5, Nickname("Ethan"), ""),
    )

val MEMBERS_RESPONSE =
    listOf(
        MemberResponse(1, "Alice", ""),
        MemberResponse(2, "Bob", ""),
        MemberResponse(3, "Charlie", ""),
        MemberResponse(4, "Diana", ""),
        MemberResponse(5, "Ethan", ""),
    )
