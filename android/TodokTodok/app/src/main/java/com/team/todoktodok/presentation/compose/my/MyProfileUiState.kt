package com.team.todoktodok.presentation.compose.my

import com.team.domain.model.Book
import com.team.domain.model.Discussion
import com.team.domain.model.member.Profile
import com.team.todoktodok.presentation.compose.my.books.MyBooksUiModel
import com.team.todoktodok.presentation.compose.my.liked.LikedDiscussionsUiState
import com.team.todoktodok.presentation.compose.my.participated.ParticipatedDiscussionsUiState

data class MyProfileUiState(
    val profile: Profile = Profile.EMPTY,
    val memberId: Long = INITIAL_MEMBER_ID,
    val activatedBooks: MyBooksUiModel = MyBooksUiModel(),
    val likedDiscussions: LikedDiscussionsUiState = LikedDiscussionsUiState(),
    val participatedDiscussions: ParticipatedDiscussionsUiState = ParticipatedDiscussionsUiState(),
) {
    fun setMemberId(memberId: Long): MyProfileUiState = copy(memberId = memberId)

    fun setActivatedBooks(updatedBooks: List<Book>): MyProfileUiState = copy(activatedBooks = activatedBooks.setBooks(updatedBooks))

    fun setParticipatedDiscussions(updatedDiscussions: List<Discussion>): MyProfileUiState =
        copy(
            participatedDiscussions =
                participatedDiscussions.setDiscussion(
                    discussions = updatedDiscussions,
                    memberId = memberId,
                ),
        )

    fun setLikedDiscussions(updatedDiscussions: List<Discussion>): MyProfileUiState =
        copy(likedDiscussions = likedDiscussions.setDiscussion(updatedDiscussions))

    fun modifyProfile(updatedProfile: Profile): MyProfileUiState = copy(profile = updatedProfile)

    fun toggleShowMyDiscussion(isShow: Boolean): MyProfileUiState =
        copy(participatedDiscussions = participatedDiscussions.toggleShowMyDiscussion(isShow))

    fun modifyProfileImage(profileImage: String): MyProfileUiState =
        copy(
            profile = profile.copy(profileImage = profileImage),
            likedDiscussions =
                likedDiscussions.modifyMyDiscussionProfileImage(
                    profileImage,
                    memberId,
                ),
            participatedDiscussions =
                participatedDiscussions.modifyMyDiscussionProfileImage(profileImage, memberId),
        )

    companion object {
        private const val INITIAL_MEMBER_ID = -1L
    }
}
