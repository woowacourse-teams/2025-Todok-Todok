package com.team.todoktodok.presentation.xml.discussion.create.draft

sealed interface DraftUiEvent {
    data class NavigateToCreateDiscussionRoom(
        val id: Long,
    ) : DraftUiEvent
}