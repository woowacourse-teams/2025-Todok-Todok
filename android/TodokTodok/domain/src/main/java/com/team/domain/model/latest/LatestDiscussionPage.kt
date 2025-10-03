package com.team.domain.model.latest

import com.team.domain.model.Discussion
import com.team.domain.model.PageInfo

data class LatestDiscussionPage(
    val discussions: List<Discussion>,
    val pageInfo: PageInfo,
)
