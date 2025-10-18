package com.team.todoktodok.presentation.core.utils

import android.content.Context
import android.content.Intent
import com.team.todoktodok.R
import com.team.todoktodok.presentation.core.link.ShareLinks

fun Context.shareDiscussionLink(
    id: Long,
    title: String,
) {
    val url = ShareLinks.discussionUrl(id)
    val urlMessage = "$title\n$url"
    val intent =
        Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.discussion_link_title))
            putExtra(Intent.EXTRA_TEXT, urlMessage)
        }
    startActivity(Intent.createChooser(intent, getString(R.string.share_discussion_title)))
}
