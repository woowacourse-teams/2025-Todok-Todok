package com.team.todoktodok.presentation.view.discussion.create

import com.team.todoktodok.R

enum class ErrorCreateDiscussionType(
    val id: Int,
) {
    BOOK_INFO_NOT_FOUND(R.string.error_create_discussion_book_info_not_found),
    TITLE_NOT_FOUND(R.string.error_create_discussion_title_not_found),
    CONTENT_NOT_FOUND(R.string.error_create_discussion_content_not_found),
    NOT_MY_DISCUSSION_ROOM(R.string.error_create_discussion_not_my_discussion_room),
    DISCUSSION_ROOM_INFO_NOT_FOUND(R.string.error_create_discussion_discussion_room_info_not_found),
    PLEASE_TRY_AGAIN(R.string.error_create_discussion_please_try_again),
}
