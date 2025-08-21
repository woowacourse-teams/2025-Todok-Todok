package com.team.domain.model.active

import com.team.domain.model.latest.PageInfo

data class ActivatedDiscussionPage(
    val data: List<ActivatedDiscussion>,
    val pageInfo: PageInfo,
)
