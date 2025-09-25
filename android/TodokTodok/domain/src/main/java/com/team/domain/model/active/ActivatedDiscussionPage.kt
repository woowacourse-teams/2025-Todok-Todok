package com.team.domain.model.active

import com.team.domain.model.Discussion
import com.team.domain.model.latest.PageInfo

data class ActivatedDiscussionPage(
    val data: List<Discussion>,
    val pageInfo: PageInfo,
)
