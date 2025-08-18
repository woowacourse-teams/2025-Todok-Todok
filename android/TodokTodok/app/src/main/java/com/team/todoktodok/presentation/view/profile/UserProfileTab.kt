package com.team.todoktodok.presentation.view.profile

import androidx.annotation.StringRes
import com.team.todoktodok.R

enum class UserProfileTab(
    @field:StringRes val titleResourceId: Int,
) {
    ACTIVATED_BOOKS(R.string.profile_active_books),
    CREATED_DISCUSSIONS(R.string.profile_created_discussion_room),
    PARTICIPATED_DISCUSSIONS(R.string.profile_participated_discussion_room),
    ;

    companion object {
        fun UserProfileTab(index: Int?): UserProfileTab =
            when (index) {
                0 -> ACTIVATED_BOOKS
                1 -> CREATED_DISCUSSIONS
                2 -> PARTICIPATED_DISCUSSIONS
                else -> throw IllegalArgumentException(INVALID_INDEX__EXCEPTION_MESSAGE.format(index))
            }

        private const val INVALID_INDEX__EXCEPTION_MESSAGE = "존재하지 않는 탭 인덱스입니다. %d"
    }
}
