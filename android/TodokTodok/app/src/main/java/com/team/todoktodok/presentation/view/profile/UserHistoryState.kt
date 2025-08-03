package com.team.todoktodok.presentation.view.profile

import com.team.domain.model.Books
import com.team.domain.model.Discussion

data class UserHistoryState(
    val activatedBooks: Books,
    val createdDiscussionRooms: List<Discussion>,
    val participatingDiscussionRooms: List<Discussion>,
)
