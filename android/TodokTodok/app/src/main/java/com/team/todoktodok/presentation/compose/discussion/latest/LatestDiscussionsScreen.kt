package com.team.todoktodok.presentation.compose.discussion.latest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.team.domain.model.PageInfo
import com.team.todoktodok.App
import com.team.todoktodok.R
import com.team.todoktodok.presentation.compose.LocalUiExceptionHandler
import com.team.todoktodok.presentation.compose.core.ObserveAsEvents
import com.team.todoktodok.presentation.compose.core.component.AlertSnackBar
import com.team.todoktodok.presentation.compose.core.component.CloverProgressBar
import com.team.todoktodok.presentation.compose.core.component.DiscussionCard
import com.team.todoktodok.presentation.compose.core.component.InfinityLazyColumn
import com.team.todoktodok.presentation.compose.discussion.latest.vm.LatestDiscussionViewModel
import com.team.todoktodok.presentation.compose.discussion.latest.vm.LatestDiscussionViewModelFactory
import com.team.todoktodok.presentation.compose.preview.LatestDiscussionsPreviewParameterProvider
import com.team.todoktodok.presentation.compose.theme.Green1A
import com.team.todoktodok.presentation.compose.theme.White
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LatestDiscussionsScreen(
    modifier: Modifier = Modifier,
    viewModel: LatestDiscussionViewModel =
        viewModel(
            factory = LatestDiscussionViewModelFactory((LocalContext.current.applicationContext as App).container),
        ),
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val uiExceptionHandler = LocalUiExceptionHandler.current

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle()

    val pullToRefreshState = rememberPullToRefreshState()

    val showSnackbar: (String) -> Unit = { message ->
        coroutineScope.launch {
            snackbarHostState.showSnackbar(message = message)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadLatestDiscussions()
    }

    ObserveAsEvents(viewModel.uiEvent) { event ->
        when (event) {
            is LatestDiscussionsUiEvent.ShowErrorMessage -> {
                val message =
                    context.getString(uiExceptionHandler.messageConverter(event.exception))
                showSnackbar(message)
            }
        }
    }

    ObserveAsEvents(viewModel.isRestoring) {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(context.getString(R.string.network_try_connection))
        }
    }

    LatestDiscussionsScreen(
        uiState = uiState.value,
        isLoading = isLoading.value,
        snackbarHostState = snackbarHostState,
        pullToRefreshState = pullToRefreshState,
        onLoadMore = { viewModel.loadLatestDiscussions() },
        onRefresh = viewModel::refreshLatestDiscussions,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LatestDiscussionsScreen(
    uiState: LatestDiscussionsUiState,
    isLoading: Boolean,
    snackbarHostState: SnackbarHostState,
    pullToRefreshState: PullToRefreshState,
    onLoadMore: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier,
        state = pullToRefreshState,
        indicator = {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = uiState.isRefreshing,
                containerColor = White,
                color = Green1A,
                state = pullToRefreshState,
            )
        },
    ) {
        InfinityLazyColumn(
            loadMore = { onLoadMore() },
            verticalArrangement = Arrangement.spacedBy(15.dp),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
            content = {
                items(
                    items = uiState.discussions,
                    key = { it.discussionId },
                ) { item ->
                    DiscussionCard(
                        uiState = item,
                        discussionCardType = uiState.type,
                        modifier = Modifier.padding(vertical = 2.dp),
                    )
                }

                if (isLoading) {
                    item { ProgressBar() }
                }

                if (uiState.isLastPage) {
                    item { LastPageGuideText() }
                }
            },
        )

        SnackbarHost(
            hostState = snackbarHostState,
            snackbar = { AlertSnackBar(snackbarData = it) },
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

@Composable
private fun ProgressBar() {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        CloverProgressBar(
            visible = true,
            size = 60.dp,
        )
    }
}

@Composable
private fun LastPageGuideText() {
    Text(
        text = stringResource(R.string.last_discussion_page_guide),
        style = MaterialTheme.typography.labelSmall,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun DiscussionsScreenPreview(
    @PreviewParameter(LatestDiscussionsPreviewParameterProvider::class)
    latestDiscussionsUiState: LatestDiscussionsUiState,
) {
    LatestDiscussionsScreen(
        uiState = latestDiscussionsUiState,
        isLoading = true,
        snackbarHostState = SnackbarHostState(),
        pullToRefreshState = rememberPullToRefreshState(),
        onLoadMore = {},
        onRefresh = {},
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun LoadingDiscussionsScreenPreview() {
    LatestDiscussionsScreen(
        uiState = LatestDiscussionsUiState(),
        isLoading = true,
        snackbarHostState = SnackbarHostState(),
        pullToRefreshState = rememberPullToRefreshState(),
        onLoadMore = {},
        onRefresh = {},
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun LastPageDiscussionsScreenPreview() {
    LatestDiscussionsScreen(
        uiState =
            LatestDiscussionsUiState(
                latestPage =
                    PageInfo(
                        hasNext = false,
                        nextCursor = null,
                    ),
            ),
        isLoading = false,
        snackbarHostState = SnackbarHostState(),
        pullToRefreshState = rememberPullToRefreshState(),
        onLoadMore = {},
        onRefresh = {},
    )
}
