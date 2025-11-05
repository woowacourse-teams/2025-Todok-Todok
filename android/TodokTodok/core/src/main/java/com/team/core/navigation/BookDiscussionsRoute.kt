package com.team.core.navigation

import android.app.Activity

interface BookDiscussionsRoute {
    fun navigateToBookDiscussions(
        fromActivity: Activity,
        bookId: Long,
    )
}
