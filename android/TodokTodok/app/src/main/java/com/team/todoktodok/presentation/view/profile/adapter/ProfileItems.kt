package com.team.todoktodok.presentation.view.profile.adapter

import com.team.domain.model.member.Profile
import com.team.todoktodok.presentation.view.profile.UserHistoryState

sealed class ProfileItems(
    val viewType: ViewType,
) {
    data object HeaderItem : ProfileItems(ViewType.VIEW_TYPE_HEADER)

    data class InformationItem(
        val value: Profile,
    ) : ProfileItems(ViewType.VIEW_TYPE_INFORMATION)

    data object TabItem : ProfileItems(ViewType.VIEW_TYPE_TAB)

    data class HistoryItem(
        val value: UserHistoryState,
    ) : ProfileItems(ViewType.VIEW_TYPE_HISTORY)

    enum class ViewType {
        VIEW_TYPE_HEADER,
        VIEW_TYPE_INFORMATION,
        VIEW_TYPE_TAB,
        VIEW_TYPE_HISTORY,
    }
}
