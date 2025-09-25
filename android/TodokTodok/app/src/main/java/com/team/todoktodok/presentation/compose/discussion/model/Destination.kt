package com.team.todoktodok.presentation.compose.discussion.model

import com.team.todoktodok.R

enum class Destination(
    val label: Int,
    val contentDescription: Int,
) {
    HOT(R.string.discussion_tab_title_hot, R.string.discussion_tab_title_hot),
    ALL(R.string.discussion_tab_title_all, R.string.discussion_tab_title_all),
    MY(R.string.discussion_tab_title_my, R.string.discussion_tab_title_my),
    ;

    companion object {
        fun Destination(index: Int): Destination = entries[index]
    }
}
