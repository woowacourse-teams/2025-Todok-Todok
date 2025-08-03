package com.team.todoktodok.presentation.view.profile.adapter

import com.team.domain.model.member.Profile

sealed class ProfileItems(
    val viewType: ViewType,
) {
    data object HeaderItem : ProfileItems(ViewType.VIEW_TYPE_HEADER)

    data class InformationItem(
        val value: Profile,
    ) : ProfileItems(ViewType.VIEW_TYPE_INFORMATION)

    data object TabItem : ProfileItems(ViewType.VIEW_TYPE_TAB)

    data class ContentItem(
        val value: List<UserContentItems>,
    ) : ProfileItems(ViewType.VIEW_TYPE_CONTENT)

    enum class ViewType {
        VIEW_TYPE_HEADER,
        VIEW_TYPE_INFORMATION,
        VIEW_TYPE_TAB,
        VIEW_TYPE_CONTENT,
    }
}
