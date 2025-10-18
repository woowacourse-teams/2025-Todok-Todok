package com.team.todoktodok.presentation.core.link

import android.net.Uri

object ShareLinks {
    const val HOST = "todoktodok.com"
    const val SCHEME = "https"
    const val PATH_DISCUSSION = "discussiondetail"

    fun discussionUrl(id: Long): String =
        Uri
            .Builder()
            .scheme(SCHEME)
            .authority(HOST)
            .appendPath(PATH_DISCUSSION)
            .appendPath(id.toString())
            .build()
            .toString()
}
