package com.team.todoktodok.presentation.compose.my.model

import com.team.domain.model.Book
import com.team.domain.model.member.Profile
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionUiState
import com.team.todoktodok.presentation.compose.my.books.MyBooksUiState

data class MyProfileUiState(
    val profile: Profile = Profile.EMPTY,
    val activatedBooks: MyBooksUiState = MyBooksUiState(),
    val participatedDiscussions: List<DiscussionUiState> = emptyList(),
    val createdDiscussions: List<DiscussionUiState> = emptyList(),
) {
    fun modifyProfile(updatedProfile: Profile): MyProfileUiState = copy(profile = updatedProfile)

    fun addActivatedBooks(updatedBooks: List<Book>): MyProfileUiState = copy(activatedBooks = activatedBooks.copy(books = updatedBooks))
}
