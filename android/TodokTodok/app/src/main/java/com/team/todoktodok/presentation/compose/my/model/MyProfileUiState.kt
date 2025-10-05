package com.team.todoktodok.presentation.compose.my.model

import com.team.domain.model.Book
import com.team.domain.model.Discussion
import com.team.domain.model.member.Profile
import com.team.todoktodok.presentation.compose.my.books.MyBooksUiModel
import com.team.todoktodok.presentation.compose.my.participated.ParticipatedDiscussionsUiModel

data class MyProfileUiState(
    val profile: Profile = Profile.EMPTY,
    val activatedBooks: MyBooksUiModel = MyBooksUiModel(),
    val participatedDiscussions: ParticipatedDiscussionsUiModel = ParticipatedDiscussionsUiModel(),
) {
    fun setMemberId(memberId: Long): MyProfileUiState = copy(participatedDiscussions = participatedDiscussions.setMemberId(memberId))

    fun modifyProfile(updatedProfile: Profile): MyProfileUiState = copy(profile = updatedProfile)

    fun setActivatedBooks(updatedBooks: List<Book>): MyProfileUiState = copy(activatedBooks = activatedBooks.setBooks(updatedBooks))

    fun addParticipatedDiscussions(updatedDiscussions: List<Discussion>): MyProfileUiState =
        copy(participatedDiscussions = participatedDiscussions.add(updatedDiscussions))

    fun toggleShowMyDiscussion(isShow: Boolean): MyProfileUiState =
        copy(participatedDiscussions = participatedDiscussions.toggleShowMyDiscussion(isShow))
}
