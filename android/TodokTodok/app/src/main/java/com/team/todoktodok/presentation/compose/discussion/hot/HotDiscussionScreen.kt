package com.team.todoktodok.presentation.compose.discussion.hot

import android.app.Activity.RESULT_OK
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.team.todoktodok.App
import com.team.todoktodok.R
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
import com.team.todoktodok.presentation.compose.theme.Green1A
import com.team.todoktodok.presentation.compose.theme.White
import com.team.todoktodok.presentation.xml.discussiondetail.DiscussionDetailActivity

@OptIn(ExperimentalMaterial3Api::class)
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
    val activityResultLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                viewModel.loadHotDiscussions()
            }
        }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val exceptionHandler = LocalUiExceptionHandler.current
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle()
    val pullToRefreshState = rememberPullToRefreshState()

    LaunchedEffect(Unit) {
        viewModel.loadHotDiscussions()
    }

    ObserveAsEvents(viewModel.uiEvent) { event ->
        when (event) {
            is HotDiscussionUiEvent.ShowErrorMessage -> {
                val message = context.getString(exceptionHandler.messageConverter(event.exception))
                exceptionHandler.showErrorMessage(coroutineScope, message)
            }
        }
    }

    HotDiscussionScreen(
        uiState = uiState.value,
        isLoading = isLoading.value,
        onLoadMore = viewModel::loadNextActivatedDiscussions,
        onRefresh = viewModel::refreshHotDiscussions,
        pullToRefreshState = pullToRefreshState,
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
private fun HotDiscussionScreen(
    uiState: HotDiscussionUiState,
    pullToRefreshState: PullToRefreshState,
    isLoading: Boolean,
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
            loadMoreLimitCount = 3,
            contentPadding = PaddingValues(vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp),
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(10.dp),
        ) {
            item {
                PopularDiscussionsScreen(
                    uiState = uiState.popularDiscussions,
                    onClickDiscussion = { onClickDiscussion(it) },
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
                    onClick = { onClickDiscussion(it) },
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
            if (!uiState.hasNextPage) {
                item { LastPageGuideText() }
            }
        }
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
private fun HotDiscissionContent(
    @PreviewParameter(HotDiscussionPreviewParameterProvider::class)
    hotDiscussionUiState: HotDiscussionUiState,
) {
    HotDiscussionScreen(
        uiState = hotDiscussionUiState,
        pullToRefreshState = rememberPullToRefreshState(),
        isLoading = false,
        onLoadMore = {},
        onClickDiscussion = {},
        onRefresh = {},
    )
}
