package com.team.ui_compose.discussion.model

import android.content.Intent
import com.team.core.extension.getParcelableCompat
import com.team.core.serialization.SerializationDiscussion

sealed interface DiscussionResult {
    data class Deleted(
        val id: Long,
    ) : DiscussionResult

    data class Watched(
        val discussion: SerializationDiscussion,
    ) : DiscussionResult

    data object None : DiscussionResult

    companion object {
        const val EXTRA_DELETE_DISCUSSION = "delete_discussion"
        const val EXTRA_WATCHED_DISCUSSION = "watched_discussion"
        const val DEFAULT_DISCUSSION_ID = -1L

        fun fromIntent(intent: Intent): DiscussionResult =
            when {
                intent.hasExtra(EXTRA_DELETE_DISCUSSION) -> {
                    val deletedId = intent.getLongExtra(EXTRA_DELETE_DISCUSSION, DEFAULT_DISCUSSION_ID)
                    if (deletedId != DEFAULT_DISCUSSION_ID) {
                        Deleted(deletedId)
                    } else {
                        None
                    }
                }

                intent.hasExtra(EXTRA_WATCHED_DISCUSSION) -> {
                    intent.getParcelableCompat<SerializationDiscussion>(EXTRA_WATCHED_DISCUSSION)?.let {
                        Watched(it)
                    } ?: None
                }

                else -> None
            }
    }
}
