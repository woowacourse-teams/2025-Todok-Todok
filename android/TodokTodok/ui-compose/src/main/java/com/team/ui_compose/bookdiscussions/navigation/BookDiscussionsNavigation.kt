package com.team.ui_compose.bookdiscussions.navigation

import android.app.Activity
import com.team.core.navigation.BookDiscussionsRoute
import com.team.ui_compose.bookdiscussions.BookDiscussionsActivity
import javax.inject.Inject

class BookDiscussionsNavigation
    @Inject
    constructor() : BookDiscussionsRoute {
        override fun navigateToBookDiscussions(
            fromActivity: Activity,
            bookId: Long,
        ) {
            val intent = BookDiscussionsActivity.Intent(fromActivity, bookId)
            fromActivity.startActivity(intent)
        }
    }
