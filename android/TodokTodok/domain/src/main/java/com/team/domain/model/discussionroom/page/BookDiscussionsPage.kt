package com.team.domain.model.discussionroom.page

import com.team.domain.model.Discussion
import com.team.domain.model.PageInfo

data class BookDiscussionsPage(
    val discussions: List<Discussion>,
    val pageInfo: PageInfo,
)
