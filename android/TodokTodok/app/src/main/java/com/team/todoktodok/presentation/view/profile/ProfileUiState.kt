package com.team.todoktodok.presentation.view.profile

import com.team.domain.model.Book
import com.team.domain.model.Discussion
import com.team.domain.model.member.MemberId
import com.team.domain.model.member.Profile
import com.team.todoktodok.presentation.view.profile.adapter.ProfileItems

data class ProfileUiState(
    val items: List<ProfileItems> = emptyList(),
    val activatedBooks: List<Book> = emptyList(),
    val participatedDiscussions: List<Discussion> = emptyList(),
    val createdDiscussions: List<Discussion> = emptyList(),
    val memberId: MemberId = MemberId.Mine,
    val isMyProfilePage: Boolean = false,
    val isLoading: Boolean = false,
) {
    fun toggleLoading(isLoading: Boolean): ProfileUiState = copy(isLoading = isLoading)

    fun modifyProfile(profile: Profile): ProfileUiState {
        if (items.size <= PROFILE_INFORMATION_INDEX) return this

        val currentItems = items.toMutableList()
        currentItems[PROFILE_INFORMATION_INDEX] =
            ProfileItems.InformationItem(profile, isMyProfilePage)

        return copy(items = currentItems)
    }

    companion object {
        fun initial(
            memberId: MemberId,
            profile: Profile,
            books: List<Book>,
            joinedDiscussions: List<Discussion>,
            createdDiscussions: List<Discussion>,
        ): ProfileUiState {
            val isMyProfilePage = memberId is MemberId.Mine
            val initialItems =
                listOf(
                    ProfileItems.HeaderItem(isMyProfilePage),
                    ProfileItems.InformationItem(profile, isMyProfilePage),
                    ProfileItems.TabItem,
                )

            return ProfileUiState(
                initialItems,
                books,
                joinedDiscussions,
                createdDiscussions,
                memberId,
                isMyProfilePage,
            )
        }

        private const val PROFILE_INFORMATION_INDEX = 1
    }
}
