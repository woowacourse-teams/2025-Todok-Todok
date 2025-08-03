package com.team.todoktodok.presentation.view.profile.adapter

import com.team.domain.model.member.Profile

sealed class ProfileItems(
    val viewType: ViewType,
) {
    data object HeaderItem : ProfileItems(ViewType.HEADER)

    data class InformationItem(
        val value: Profile,
    ) : ProfileItems(ViewType.INFORMATION)

    data object TabItem : ProfileItems(ViewType.TAB)

    data class ContentItem(
        val value: List<ContentItems>,
    ) : ProfileItems(ViewType.CONTENT)

    enum class ViewType {
        HEADER,
        INFORMATION,
        TAB,
        CONTENT,
    }
}
