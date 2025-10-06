package com.team.todoktodok.presentation.xml.discussion.create

import com.team.todoktodok.R

enum class ErrorCreateDiscussionType(
    val id: Int,
) {
    BOOK_INFO_NOT_FOUND(R.string.error_create_discussion_book_info_not_found),
    TITLE_NOT_FOUND(R.string.error_create_discussion_title_not_found),
    CONTENT_NOT_FOUND(R.string.error_create_discussion_content_not_found),
    NOT_MY_DISCUSSION_ROOM(R.string.error_create_discussion_not_my_discussion_room),
    DISCUSSION_ROOM_INFO_NOT_FOUND(R.string.error_create_discussion_discussion_room_info_not_found),
    TITLE_LENGTH_OVER(R.string.error_create_discussion_title_length_over),
    OPINION_LENGTH_OVER(R.string.error_create_discussion_opinion_length_over),

    TITLE_AND_CONTENT_NOT_FOUND(R.string.error_create_discussion_title_and_content_not_found),
}
