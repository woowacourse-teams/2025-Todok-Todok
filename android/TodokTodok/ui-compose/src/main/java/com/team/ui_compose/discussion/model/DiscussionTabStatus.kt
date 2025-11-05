package com.team.ui_compose.discussion.model

import com.team.ui_compose.R

enum class DiscussionTabStatus(
    val label: Int,
    val contentDescription: Int,
) {
    HOT(R.string.discussion_tab_title_hot, R.string.discussion_tab_title_hot),
    ALL(R.string.discussion_tab_title_all, R.string.discussion_tab_title_all),
    ;

    companion object {
        fun DiscussionTabStatus(index: Int): DiscussionTabStatus = entries[index]
    }
}
