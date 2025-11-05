package com.team.core.navigation

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher

interface DiscussionDetailRoute {
    fun navigateToDiscussionDetail(
        fromActivity: Activity,
        discussionId: Long,
    )

    fun navigateToDiscussionDetailForResult(
        fromActivity: Activity,
        discussionId: Long,
        resultLauncher: ActivityResultLauncher<Intent>,
    )
}
