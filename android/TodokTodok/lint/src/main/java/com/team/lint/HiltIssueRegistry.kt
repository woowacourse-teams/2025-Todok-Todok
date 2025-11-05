package com.team.lint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.CURRENT_API
import com.android.tools.lint.detector.api.Issue
import com.team.lint.MissingHiltViewModelDetector.Companion.ISSUE_MISSING_HILT_VIEWMODEL

class HiltIssueRegistry : IssueRegistry() {
    override val issues: List<Issue>
        get() = listOf(ISSUE_MISSING_HILT_VIEWMODEL)

    override val api: Int
        get() = CURRENT_API
}
