package com.team.todoktodok.presentation.xml.draft

sealed interface DraftUiEvent {
    data class NavigateToCreateDiscussionRoom(
        val id: Long,
    ) : DraftUiEvent
}
