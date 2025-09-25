package com.team.todoktodok.presentation.compose.discussion

import SearchDiscussionBar
import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.todoktodok.R
import com.team.todoktodok.presentation.compose.core.ObserveAsEvents
import com.team.todoktodok.presentation.compose.core.component.CloverProgressBar
import com.team.todoktodok.presentation.compose.discussion.component.DiscussionToolbar
import com.team.todoktodok.presentation.compose.discussion.model.Destination
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionFAB
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionTab
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionsUiEvent
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionsUiState
import com.team.todoktodok.presentation.compose.discussion.vm.DiscussionsViewModel
import com.team.todoktodok.presentation.compose.theme.Black18
import com.team.todoktodok.presentation.compose.theme.GreenF0
import com.team.todoktodok.presentation.compose.theme.White
import com.team.todoktodok.presentation.core.ExceptionMessageConverter
import com.team.todoktodok.presentation.core.component.CloverProgressBar
import com.team.todoktodok.presentation.xml.profile.UserProfileTab
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscussionsScreen(
    exceptionMessageConverter: ExceptionMessageConverter,
    viewModel: DiscussionsViewModel,
    onClickNotification: () -> Unit,
    onClickProfile: () -> Unit,
    onClickCreateDiscussion: () -> Unit,
    onDiscussionClick: (Long) -> Unit,
    onClickMyDiscussionHeader: (UserProfileTab) -> Unit,
    modifier: Modifier = Modifier,
    timeoutMillis: Long = 1500L,
) {
    val pagerState =
        rememberPagerState(initialPage = Destination.HOT.ordinal) { Destination.entries.size }
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    var snackbarJob by remember { mutableStateOf<Job?>(null) }
    var lastBackPressed by rememberSaveable { mutableLongStateOf(0L) }

    val showMessage: (String, Long) -> Unit = { message, millis ->
        snackbarJob?.cancel()
        snackbarJob =
            coroutineScope.launch {
                val showJob =
                    launch {
                        snackbarHostState.showSnackbar(
                            message = message,
                            duration = SnackbarDuration.Indefinite,
                        )
                    }
                delay(millis)
                snackbarHostState.currentSnackbarData?.dismiss()
                showJob.cancel()
            }
    }

    BackHandler(enabled = true) {
        val now = System.currentTimeMillis()
        if (now - lastBackPressed <= timeoutMillis) {
            (context as? Activity)?.finishAffinity()
        } else {
            lastBackPressed = now
            snackbarHostState.currentSnackbarData?.dismiss()
            showMessage(context.getString(R.string.press_back_again_to_exit), timeoutMillis)
        }
    }

    ObserveAsEvents(viewModel.uiEvent) { event ->
        when (event) {
            is DiscussionsUiEvent.ShowErrorMessage -> {
                val message = context.getString(exceptionMessageConverter(event.exception))
                showMessage(message, timeoutMillis)
            }

            DiscussionsUiEvent.ScrollToAllDiscussion ->
                coroutineScope.launch {
                    pagerState.animateScrollToPage(Destination.ALL.ordinal)
                }
        }
    }

    ObserveAsEvents(viewModel.isRestoring) {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(context.getString(R.string.network_try_connection))
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadIsUnreadNotification()
        viewModel.loadHotDiscussions()
        viewModel.loadLatestDiscussions()
        viewModel.loadMyDiscussions()
    }

    DiscussionsScreen(
        isLoading = isLoading.value,
        uiState = uiState.value,
        pagerState = pagerState,
        snackbarHostState = snackbarHostState,
        onDiscussionClick = onDiscussionClick,
        onClickNotification = onClickNotification,
        onClickProfile = onClickProfile,
        onClickMyDiscussionHeader = onClickMyDiscussionHeader,
        onSearchKeywordChanged = viewModel::modifySearchKeyword,
        onSearch = viewModel::loadSearchedDiscussions,
        onLatestDiscussionLoadMore = viewModel::loadLatestDiscussions,
        onActivatedDiscussionLoadMore = viewModel::loadActivatedDiscussions,
        onRefresh = viewModel::refreshLatestDiscussions,
        onClickCreateDiscussion = onClickCreateDiscussion,
        modifier = modifier,
    )
}

@Composable
fun DiscussionsScreen(
    isLoading: Boolean,
    uiState: DiscussionsUiState,
    pagerState: PagerState,
    snackbarHostState: SnackbarHostState,
    onDiscussionClick: (Long) -> Unit,
    onClickMyDiscussionHeader: (UserProfileTab) -> Unit,
    onSearchKeywordChanged: (String) -> Unit,
    onClickNotification: () -> Unit,
    onClickProfile: () -> Unit,
    onSearch: () -> Unit,
    onLatestDiscussionLoadMore: () -> Unit,
    onActivatedDiscussionLoadMore: () -> Unit,
    onRefresh: () -> Unit,
    onClickCreateDiscussion: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            DiscussionToolbar(
                isExistNotification = uiState.isUnreadNotification,
                onClickNotification = { onClickNotification() },
                onClickProfile = { onClickProfile() },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .statusBarsPadding(),
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = {
                    Snackbar(
                        snackbarData = it,
                        containerColor = GreenF0,
                        contentColor = Black18,
                    )
                },
            )
        },
        floatingActionButton = {
            DiscussionFAB(onClickCreateDiscussion = { onClickCreateDiscussion() })
        },
    ) { innerPadding ->
        val searchDiscussion = uiState.allDiscussions.searchDiscussion

        Box(
            contentAlignment = Alignment.Center,
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
        ) {
            Column(
                modifier =
                    modifier
                        .background(color = White),
            ) {
                SearchDiscussionBar(
                    onSearch = { onSearch() },
                    searchKeyword = searchDiscussion.searchKeyword,
                    previousKeyword = searchDiscussion.previousKeyword,
                    onKeywordChange = { onSearchKeywordChanged(it) },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                )

                DiscussionTab(
                    uiState,
                    pagerState = pagerState,
                    onLatestDiscussionLoadMore = { onLatestDiscussionLoadMore() },
                    onActivatedDiscussionLoadMore = { onActivatedDiscussionLoadMore() },
                    onRefresh = { onRefresh() },
                    onClick = onDiscussionClick,
                    onClickMyDiscussionHeader = onClickMyDiscussionHeader,
                )
            }
            CloverProgressBar(isLoading)
        }
    }
}

@Preview
@Composable
private fun DiscussionsScreenPreview() {
    DiscussionsScreen(
        isLoading = true,
        uiState = DiscussionsUiState(),
        pagerState = rememberPagerState(0) { 3 },
        snackbarHostState = remember { SnackbarHostState() },
        onDiscussionClick = {},
        onClickMyDiscussionHeader = {},
        onSearchKeywordChanged = {},
        onClickNotification = {},
        onClickProfile = {},
        onSearch = {},
        onLatestDiscussionLoadMore = {},
        onActivatedDiscussionLoadMore = {},
        onRefresh = {},
        onClickCreateDiscussion = {},
    )
}
