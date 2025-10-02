package com.team.todoktodok.presentation.compose.discussion.all

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.team.todoktodok.App
import com.team.todoktodok.presentation.compose.discussion.all.vm.AllDiscussionViewModel
import com.team.todoktodok.presentation.compose.discussion.all.vm.AllDiscussionViewModelFactory
import com.team.todoktodok.presentation.compose.discussion.latest.LatestDiscussionsScreen
import com.team.todoktodok.presentation.compose.discussion.model.AllDiscussionMode
import com.team.todoktodok.presentation.compose.discussion.search.SearchDiscussionScreen

@Composable
fun AllDiscussionsScreen(
    modifier: Modifier = Modifier,
    viewModel: AllDiscussionViewModel =
        viewModel(
            factory = AllDiscussionViewModelFactory((LocalContext.current.applicationContext as App).container),
        ),
) {
    LaunchedEffect(Unit) {
        viewModel.loadLatestDiscussions()
    }

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    AllDiscussionsScreen(
        uiState = uiState.value,
        modifier = modifier,
    )
}

@Composable
fun AllDiscussionsScreen(
    uiState: AllDiscussionUiState,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
    ) {
        when (uiState.mode) {
            AllDiscussionMode.LATEST ->
                LatestDiscussionsScreen(
                    modifier =
                        Modifier
                            .fillMaxSize(),
                )

            AllDiscussionMode.SEARCH ->
                SearchDiscussionScreen(
                    uiState = uiState.searchDiscussion,
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
        uiState = AllDiscussionUiState(),
    )
}
