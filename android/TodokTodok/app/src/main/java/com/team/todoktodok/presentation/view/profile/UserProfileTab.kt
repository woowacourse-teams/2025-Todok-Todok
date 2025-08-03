package com.team.todoktodok.presentation.view.profile

enum class UserProfileTab {
    ACTIVATED_BOOKS,
    CREATED_DISCUSSIONS,
    JOINED_DISCUSSIONS,
    ;

    companion object {
        fun UserProfileTab(index: Int?): UserProfileTab =
            when (index) {
                0 -> ACTIVATED_BOOKS
                1 -> CREATED_DISCUSSIONS
                2 -> JOINED_DISCUSSIONS
                else -> throw IllegalArgumentException(INVALID_INDEX__EXCEPTION_MESSAGE.format(index))
            }

        private const val INVALID_INDEX__EXCEPTION_MESSAGE = "존재하지 않는 탭 인덱스입니다. %d"
    }
}
