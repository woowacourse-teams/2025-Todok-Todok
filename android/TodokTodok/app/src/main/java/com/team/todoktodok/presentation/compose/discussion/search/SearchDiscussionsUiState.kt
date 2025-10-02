package com.team.todoktodok.presentation.compose.discussion.search

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.team.domain.model.Discussion
import com.team.todoktodok.presentation.compose.core.component.DiscussionCardType
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionUiState
import com.team.todoktodok.presentation.compose.theme.Green1A
import com.team.todoktodok.presentation.xml.serialization.SerializationDiscussion

data class SearchDiscussionsUiState(
    val discussions: List<DiscussionUiState> = emptyList(),
    val type: DiscussionCardType.QueryHighlighting =
        DiscussionCardType.QueryHighlighting(
            EMPTY_SEARCH_KEYWORD,
        ),
    val previousKeyword: String = EMPTY_SEARCH_KEYWORD,
) {
    fun formatNotFoundGuideMessage(defaultFormat: String): AnnotatedString {
        val defaultGuideMessage = defaultFormat.format(type.keyword)
        return buildAnnotatedString {
            val keyword = type.keyword
            val startIndex = defaultGuideMessage.indexOf(keyword)
            if (startIndex >= 0) {
                append(defaultGuideMessage.substring(0, startIndex))
                withStyle(SpanStyle(color = Green1A, fontWeight = FontWeight.Bold)) {
                    append(keyword)
                }
                append(defaultGuideMessage.substring(startIndex + keyword.length))
            } else {
                append(defaultGuideMessage)
            }
        }
    }

    fun add(
        keyword: String,
        newDiscussions: List<Discussion>,
    ): SearchDiscussionsUiState {
        if (keyword == previousKeyword || keyword.isBlank()) return this
        val newDiscussions = newDiscussions.map { DiscussionUiState(it) }
        return copy(
            discussions = newDiscussions,
            previousKeyword = keyword,
        )
    }

    fun clear() =
        copy(
            discussions = emptyList(),
            previousKeyword = EMPTY_SEARCH_KEYWORD,
        )

    fun modifyKeyword(keyword: String) = copy(type = type.copy(keyword = keyword))

    fun modify(newDiscussion: SerializationDiscussion): SearchDiscussionsUiState =
        copy(
            discussions =
                discussions.map {
                    if (it.discussionId == newDiscussion.id) {
                        DiscussionUiState(newDiscussion.toDomain())
                    } else {
                        it
                    }
                },
        )

    fun remove(discussionId: Long): SearchDiscussionsUiState = copy(discussions = discussions.filter { it.discussionId != discussionId })

    companion object {
        private const val EMPTY_SEARCH_KEYWORD = ""
    }
}
