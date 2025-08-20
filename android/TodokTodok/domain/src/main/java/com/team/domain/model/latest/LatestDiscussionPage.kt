package com.team.domain.model.latest

data class LatestDiscussionPage(
    val discussions: List<LatestDiscussion>,
    val pageInfo: PageInfo,
)
