package com.team.todoktodok.presentation.compose.discussion.all

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.team.todoktodok.presentation.compose.discussion.latest.LatestDiscussionsScreen
import com.team.todoktodok.presentation.compose.discussion.model.AllDiscussionMode
import com.team.todoktodok.presentation.compose.discussion.search.SearchDiscussionScreen
import com.team.todoktodok.presentation.compose.discussion.search.SearchDiscussionsUiState

@Composable
fun AllDiscussionsScreen(
    searchDiscussion: SearchDiscussionsUiState,
    allDiscussionScreenMode: AllDiscussionMode,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
    ) {
        when (allDiscussionScreenMode) {
            AllDiscussionMode.LATEST ->
                LatestDiscussionsScreen(
                    modifier =
                        Modifier
                            .fillMaxSize(),
                )

            AllDiscussionMode.SEARCH ->
                SearchDiscussionScreen(
                    uiState = searchDiscussion,
                    modifier =
                        Modifier
                            .fillMaxSize(),
                )
        }
    }
}

@Preview
@Composable
private fun AllDiscussionsScreenPreview() {
    AllDiscussionsScreen(
        searchDiscussion = SearchDiscussionsUiState(),
        allDiscussionScreenMode = AllDiscussionMode.LATEST,
    )
}
