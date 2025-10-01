package com.team.todoktodok.presentation.compose.discussion.hot

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.team.todoktodok.App
import com.team.todoktodok.presentation.compose.LocalUiExceptionHandler
import com.team.todoktodok.presentation.compose.core.ObserveAsEvents
import com.team.todoktodok.presentation.compose.core.component.CloverProgressBar
import com.team.todoktodok.presentation.compose.core.component.DiscussionCard
import com.team.todoktodok.presentation.compose.core.component.InfinityLazyColumn
import com.team.todoktodok.presentation.compose.discussion.activate.ActivatedDiscussionHeader
import com.team.todoktodok.presentation.compose.discussion.hot.vm.HotDiscussionViewModel
import com.team.todoktodok.presentation.compose.discussion.hot.vm.HotDiscussionViewModelFactory
import com.team.todoktodok.presentation.compose.discussion.popular.PopularDiscussionsScreen
import com.team.todoktodok.presentation.compose.preview.HotDiscussionPreviewParameterProvider

@Composable
fun HotDiscussionScreen(
    modifier: Modifier = Modifier,
    viewModel: HotDiscussionViewModel =
        viewModel(
            factory =
                HotDiscussionViewModelFactory(
                    appContainer = (LocalContext.current.applicationContext as App).container,
                ),
        ),
) {
    val context = LocalContext.current
    val exceptionHandler = LocalUiExceptionHandler.current
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadHotDiscussions()
    }

    ObserveAsEvents(viewModel.uiEvent) { event ->
        when (event) {
            is HotDiscussionUiEvent.ShowErrorMessage -> {
                val message = context.getString(exceptionHandler.messageConverter(event.exception))
                exceptionHandler.snackbarHostState.showSnackbar(message)
            }
        }
    }

    HotDiscussionScreen(
        uiState = uiState.value,
        isLoading = isLoading.value,
        onLoadMore = { viewModel.loadActivatedDiscussions() },
        modifier = modifier,
    )
}

@Composable
private fun HotDiscussionScreen(
    uiState: HotDiscussionUiState,
    isLoading: Boolean,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier,
) {
    InfinityLazyColumn(
        loadMore = { onLoadMore() },
        loadMoreLimitCount = 3,
        contentPadding = PaddingValues(vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        modifier = modifier.padding(10.dp),
    ) {
        item {
            PopularDiscussionsScreen(
                uiState = uiState.popularDiscussions,
            )
        }

        item { ActivatedDiscussionHeader() }

        items(
            items = uiState.activatedDiscussions.discussions,
            key = { it.discussionId },
        ) { item ->
            DiscussionCard(
                uiState = item,
                discussionCardType = uiState.activatedDiscussions.type,
            )
        }

        if (isLoading) {
            item {
                CloverProgressBar(
                    visible = true,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .size(50.dp),
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun HotDiscissionContent(
    @PreviewParameter(HotDiscussionPreviewParameterProvider::class)
    hotDiscussionUiState: HotDiscussionUiState,
) {
    HotDiscussionScreen(
        uiState = hotDiscussionUiState,
        isLoading = true,
        onLoadMore = {},
    )
}
