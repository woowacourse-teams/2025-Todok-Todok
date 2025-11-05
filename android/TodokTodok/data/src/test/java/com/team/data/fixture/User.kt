package com.team.data.fixture

import com.team.domain.model.member.User
import com.team.data.network.response.discussion.MemberResponse

val USERS_FIXTURE =
    listOf(
        User(1, "Alice", ""),
        User(2, "Bob", ""),
        User(3, "Charlie", ""),
        User(4, "Diana", ""),
        User(5, "Ethan", ""),
    )

val MEMBERS_RESPONSE_FIXTURE =
    listOf(
        MemberResponse(1, "Alice", ""),
        MemberResponse(2, "Bob", ""),
        MemberResponse(3, "Charlie", ""),
        MemberResponse(4, "Diana", ""),
        MemberResponse(5, "Ethan", ""),
    )
