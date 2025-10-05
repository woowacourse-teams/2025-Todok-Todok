package com.team.todoktodok.presentation.compose.my

import com.team.domain.model.Book
import com.team.domain.model.Discussion
import com.team.domain.model.member.Profile
import com.team.todoktodok.presentation.compose.my.books.MyBooksUiModel
import com.team.todoktodok.presentation.compose.my.participated.ParticipatedDiscussionsUiState
import com.team.todoktodok.presentation.xml.serialization.SerializationDiscussion

data class MyProfileUiState(
    val profile: Profile = Profile.EMPTY,
    val activatedBooks: MyBooksUiModel = MyBooksUiModel(),
    val participatedDiscussions: ParticipatedDiscussionsUiState = ParticipatedDiscussionsUiState(),
) {
    fun setMemberId(memberId: Long): MyProfileUiState = copy(participatedDiscussions = participatedDiscussions.setMemberId(memberId))

    fun modifyProfile(updatedProfile: Profile): MyProfileUiState = copy(profile = updatedProfile)

    fun setActivatedBooks(updatedBooks: List<Book>): MyProfileUiState = copy(activatedBooks = activatedBooks.setBooks(updatedBooks))

    fun addParticipatedDiscussions(updatedDiscussions: List<Discussion>): MyProfileUiState =
        copy(participatedDiscussions = participatedDiscussions.add(updatedDiscussions))

    fun toggleShowMyDiscussion(isShow: Boolean): MyProfileUiState =
        copy(participatedDiscussions = participatedDiscussions.toggleShowMyDiscussion(isShow))

    fun removeDiscussion(discussionId: Long): MyProfileUiState =
        copy(participatedDiscussions = participatedDiscussions.remove(discussionId))

    fun modifyDiscussion(discussion: SerializationDiscussion): MyProfileUiState =
        copy(participatedDiscussions = participatedDiscussions.modify(discussion))
}
