package com.team.todoktodok.presentation.compose.discussion.latest

import android.app.Activity.RESULT_OK
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.team.todoktodok.presentation.compose.core.component.CloverProgressBar
import com.team.todoktodok.presentation.compose.core.component.DiscussionCard
import com.team.todoktodok.presentation.compose.core.component.InfinityLazyColumn
import com.team.todoktodok.presentation.compose.discussion.latest.vm.LatestDiscussionViewModel
import com.team.todoktodok.presentation.compose.discussion.latest.vm.LatestDiscussionViewModelFactory
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionResult
import com.team.todoktodok.presentation.compose.preview.LatestDiscussionsPreviewParameterProvider
import com.team.todoktodok.presentation.compose.theme.Green1A
import com.team.todoktodok.presentation.compose.theme.White
import com.team.todoktodok.presentation.xml.discussiondetail.DiscussionDetailActivity
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
    val activityResultLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.let { data ->
                    when (val result = DiscussionResult.fromIntent(data)) {
                        is DiscussionResult.Deleted -> viewModel.removeDiscussion(result.id)
                        is DiscussionResult.Watched -> viewModel.modifyDiscussion(result.discussion)
                        DiscussionResult.None -> Unit
                    }
                }
            }
        }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val uiExceptionHandler = LocalUiExceptionHandler.current

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle()

    val pullToRefreshState = rememberPullToRefreshState()

    LaunchedEffect(Unit) {
        viewModel.loadLatestDiscussions()
    }

    ObserveAsEvents(viewModel.uiEvent) { event ->
        when (event) {
            is LatestDiscussionsUiEvent.ShowErrorMessage -> {
                val message =
                    context.getString(uiExceptionHandler.messageConverter(event.exception))
                uiExceptionHandler.snackbarHostState.showSnackbar(message)
            }
        }
    }

    ObserveAsEvents(viewModel.isRestoring) {
        coroutineScope.launch {
            uiExceptionHandler.snackbarHostState.showSnackbar(context.getString(R.string.network_try_connection))
        }
    }

    LatestDiscussionsScreen(
        uiState = uiState.value,
        isLoading = isLoading.value,
        pullToRefreshState = pullToRefreshState,
        onLoadMore = { viewModel.loadLatestDiscussions() },
        onRefresh = viewModel::refreshLatestDiscussions,
        onClickDiscussion = {
            activityResultLauncher.launch(
                DiscussionDetailActivity.Intent(
                    context = context,
                    discussionId = it,
                ),
            )
        },
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LatestDiscussionsScreen(
    uiState: LatestDiscussionsUiState,
    isLoading: Boolean,
    pullToRefreshState: PullToRefreshState,
    onLoadMore: () -> Unit,
    onRefresh: () -> Unit,
    onClickDiscussion: (Long) -> Unit,
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
                        onClick = { onClickDiscussion(it) },
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
        pullToRefreshState = rememberPullToRefreshState(),
        onLoadMore = {},
        onRefresh = {},
        onClickDiscussion = {},
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun LoadingDiscussionsScreenPreview() {
    LatestDiscussionsScreen(
        uiState = LatestDiscussionsUiState(),
        isLoading = true,
        pullToRefreshState = rememberPullToRefreshState(),
        onLoadMore = {},
        onRefresh = {},
        onClickDiscussion = {},
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
        pullToRefreshState = rememberPullToRefreshState(),
        onLoadMore = {},
        onRefresh = {},
        onClickDiscussion = {},
    )
}
