package com.team.todoktodok.presentation.compose.my.model

import com.team.domain.model.Book
import com.team.domain.model.Discussion
import com.team.domain.model.member.Profile
import com.team.todoktodok.presentation.compose.discussion.participated.ParticipatedDiscussionsUiState
import com.team.todoktodok.presentation.compose.my.books.MyBooksUiState

data class MyProfileUiState(
    val profile: Profile = Profile.EMPTY,
    val activatedBooks: MyBooksUiState = MyBooksUiState(),
    val participatedDiscussions: ParticipatedDiscussionsUiState = ParticipatedDiscussionsUiState(),
) {
    fun modifyProfile(updatedProfile: Profile): MyProfileUiState = copy(profile = updatedProfile)

    fun addActivatedBooks(updatedBooks: List<Book>): MyProfileUiState = copy(activatedBooks = activatedBooks.copy(books = updatedBooks))

    fun addParticipatedDiscussions(updatedDiscussions: List<Discussion>): MyProfileUiState =
        copy(participatedDiscussions = participatedDiscussions.add(updatedDiscussions))
}
