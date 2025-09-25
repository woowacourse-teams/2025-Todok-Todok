package com.team.todoktodok.presentation.compose.discussion

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.todoktodok.R
import com.team.todoktodok.presentation.compose.core.ObserveAsEvents
import com.team.todoktodok.presentation.compose.discussion.component.DiscussionToolbar
import com.team.todoktodok.presentation.compose.discussion.model.Destination
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionTab
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionsUiEvent
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionsUiState
import com.team.todoktodok.presentation.compose.discussion.vm.DiscussionsViewModel
import com.team.todoktodok.presentation.compose.theme.Black18
import com.team.todoktodok.presentation.compose.theme.GreenF0
import com.team.todoktodok.presentation.core.ExceptionMessageConverter
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

    LaunchedEffect(Unit) {
        viewModel.loadIsUnreadNotification()
        viewModel.loadHotDiscussions()
        viewModel.loadLatestDiscussions()
        viewModel.loadMyDiscussions()
    }

    DiscussionsScreen(
        uiState = uiState.value,
        pagerState = pagerState,
        snackbarHostState = snackbarHostState,
        onDiscussionClick = onDiscussionClick,
        onClickMyDiscussionHeader = onClickMyDiscussionHeader,
        onSearchKeywordChanged = viewModel::modifySearchKeyword,
        onClickNotification = onClickNotification,
        onClickProfile = onClickProfile,
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
                searchKeyword = uiState.allDiscussions.searchDiscussion.searchKeyword,
                previousKeyword = uiState.allDiscussions.searchDiscussion.previousKeyword,
                isExistNotification = uiState.isUnreadNotification,
                onSearchKeywordChanged = { onSearchKeywordChanged(it) },
                onSearch = { onSearch() },
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
            FloatingActionButton(
                onClick = { onClickCreateDiscussion() },
                backgroundColor = GreenF0,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_create_discussion),
                    contentDescription = "토론방 생성",
                    tint = Color.Unspecified,
                    modifier =
                        Modifier
                            .padding(5.dp)
                            .size(60.dp),
                )
            }
        },
    ) { innerPadding ->
        DiscussionTab(
            uiState,
            pagerState = pagerState,
            onLatestDiscussionLoadMore = { onLatestDiscussionLoadMore() },
            onActivatedDiscussionLoadMore = { onActivatedDiscussionLoadMore() },
            onRefresh = { onRefresh() },
            onClick = onDiscussionClick,
            onClickMyDiscussionHeader = onClickMyDiscussionHeader,
            modifier = modifier.padding(innerPadding),
        )
    }
}

@Preview
@Composable
fun DiscussionsScreenPreview() {
    DiscussionsScreen(
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
