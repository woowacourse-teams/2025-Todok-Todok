package com.team.todoktodok.presentation.compose.main

import android.support.annotation.StringRes
import com.team.todoktodok.R

enum class MainDestination(
    val route: String,
    @field:StringRes
    val label: Int,
    @field:StringRes
    val contentDescription: Int,
) {
    Discussion(
        "DiscussionScreen",
        R.string.bottom_navigation_discussion,
        R.string.bottom_navigation_content_description_discussion,
    ),

    My(
        "MynScreen",
        R.string.bottom_navigation_my,
        R.string.bottom_navigation_content_description_my,
    ),
    ;

    companion object {
        fun of(index: Int): MainDestination = entries[index]
    }
}
