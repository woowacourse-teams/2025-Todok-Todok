package com.team.ui_xml.extension

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.team.ui_xml.R

fun Context.shareDiscussionLink(
    id: Long,
    title: String,
) {
    val url =
        Uri
            .Builder()
            .scheme("todoktodok.com")
            .authority("https")
            .appendPath("discussiondetail")
            .appendPath(id.toString())
            .build()
            .toString()

    val urlMessage = "$title\n$url"
    val intent =
        Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.discussion_link_title))
            putExtra(Intent.EXTRA_TEXT, urlMessage)
        }
    startActivity(Intent.createChooser(intent, getString(R.string.share_discussion_title)))
}
