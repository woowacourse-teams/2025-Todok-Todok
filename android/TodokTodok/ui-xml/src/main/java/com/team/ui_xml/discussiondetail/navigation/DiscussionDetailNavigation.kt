package com.team.ui_xml.discussiondetail.navigation

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.team.core.navigation.DiscussionDetailRoute
import com.team.ui_xml.discussiondetail.DiscussionDetailActivity
import javax.inject.Inject

class DiscussionDetailNavigation
    @Inject
    constructor() : DiscussionDetailRoute {
        override fun navigateToDiscussionDetail(
            fromActivity: Activity,
            discussionId: Long,
        ) {
            val intent = DiscussionDetailActivity.Intent(fromActivity, discussionId)
            fromActivity.startActivity(intent)
        }

        override fun navigateToDiscussionDetailForResult(
            fromActivity: Activity,
            discussionId: Long,
            resultLauncher: ActivityResultLauncher<Intent>,
        ) {
            val intent = DiscussionDetailActivity.Intent(fromActivity, discussionId)
            resultLauncher.launch(intent)
        }
    }
