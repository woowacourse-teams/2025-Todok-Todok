package com.team.todoktodok.presentation.view.profile

import com.team.domain.model.member.MemberId
import com.team.domain.model.member.Profile
import com.team.todoktodok.presentation.view.profile.adapter.ProfileItems

data class ProfileUiState(
    val items: List<ProfileItems>,
    val memberId: MemberId,
) {
    val isMyProfilePage get() = memberId is MemberId.Mine

    fun modifyProfile(profile: Profile): ProfileUiState {
        val currentItems = items.toMutableList()
        currentItems[PROFILE_ITEM_INDEX] = ProfileItems.InformationItem(profile, isMyProfilePage)

        return copy(items = currentItems)
    }

    companion object {
        fun initial(): ProfileUiState {
            val initialItems =
                listOf(
                    ProfileItems.HeaderItem,
                    ProfileItems.InformationItem(
                        Profile(
                            INITIALIZE_VALUE,
                            INITIALIZE_VALUE,
                            INITIALIZE_VALUE,
                            INITIALIZE_VALUE,
                        ),
                        false,
                    ),
                    ProfileItems.TabItem,
                )
            return ProfileUiState(items = initialItems, MemberId.Mine)
        }

        private const val INITIALIZE_VALUE = ""
        private const val PROFILE_ITEM_INDEX = 1
    }
}
