package com.team.domain.model.member

data class DiscussionRoom(
    private val _title: Title,
    private val _opinion: Opinion,
) {
    val title: String get() = _title.value
    val opinion: String get() = _opinion.value

    companion object {
        fun DiscussionRoom(
            title: String,
            opinion: String,
        ): DiscussionRoom = DiscussionRoom(
            _title = Title(title),
            _opinion = Opinion(opinion),
        )
    }
}



