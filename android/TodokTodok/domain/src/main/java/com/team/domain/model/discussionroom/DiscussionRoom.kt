package com.team.domain.model.discussionroom

data class DiscussionRoom(
    private val _title: DiscussionRoomTitle,
    private val _opinion: Opinion,
) {
    val title: String get() = _title.value
    val opinion: String get() = _opinion.value

    companion object {
        fun DiscussionRoom(
            title: String,
            opinion: String,
        ): DiscussionRoom =
            DiscussionRoom(
                _title = DiscussionRoomTitle(title),
                _opinion = Opinion(opinion),
            )
    }
}
