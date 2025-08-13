package com.team.todoktodok.presentation.view.profile

import com.team.domain.model.member.MemberId
import com.team.domain.model.member.Profile
import com.team.todoktodok.presentation.view.profile.adapter.ProfileItems

data class ProfileUiState(
    val items: List<ProfileItems>,
    val memberId: MemberId,
    val isMyProfilePage: Boolean,
) {
    fun modifyProfile(profile: Profile): ProfileUiState {
        val currentItems = items.toMutableList()

        currentItems[PROFILE_HEADER_INDEX] =
            ProfileItems.HeaderItem(isMyProfilePage)

        currentItems[PROFILE_INFORMATION_INDEX] =
            ProfileItems.InformationItem(profile, isMyProfilePage)

        return copy(items = currentItems)
    }

    companion object {
        fun initial(memberId: MemberId): ProfileUiState {
            val isMyProfilePage = memberId is MemberId.Mine
            val initialItems =
                listOf(
                    ProfileItems.HeaderItem(isMyProfilePage),
                    ProfileItems.InformationItem(
                        Profile(
                            0L,
                            INITIALIZE_VALUE,
                            INITIALIZE_VALUE,
                            INITIALIZE_VALUE,
                        ),
                        false,
                    ),
                    ProfileItems.TabItem,
                )
            return ProfileUiState(items = initialItems, memberId, isMyProfilePage)
        }

        private const val INITIALIZE_VALUE = ""
        private const val PROFILE_HEADER_INDEX = 0
        private const val PROFILE_INFORMATION_INDEX = 1
    }
}
