package com.team.todoktodok.presentation.xml.profile.adapter

import com.team.domain.model.member.Profile
import java.lang.IllegalArgumentException

sealed class ProfileItems(
    val viewType: ViewType,
) {
    data class HeaderItem(
        val isMyProfile: Boolean,
    ) : ProfileItems(ViewType.HEADER)

    data class InformationItem(
        val value: Profile,
        val isMyProfile: Boolean,
    ) : ProfileItems(ViewType.INFORMATION)

    data object TabItem : ProfileItems(ViewType.TAB)

    enum class ViewType(
        val sequence: Int,
    ) {
        HEADER(0),
        INFORMATION(1),
        TAB(2),
        ;

        companion object {
            fun ViewType(index: Int): ViewType =
                ViewType.entries.find { it.sequence == index }
                    ?: throw IllegalArgumentException(INVALID_INDEX_VIEW_TYPE.format(index))

            private const val INVALID_INDEX_VIEW_TYPE = "뷰타입의 인덱스가 잘못 되었습니다. [%d]"
        }
    }
}
