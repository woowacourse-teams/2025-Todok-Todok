package com.team.todoktodok.presentation.compose.discussion

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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.team.todoktodok.App
import com.team.todoktodok.R
import com.team.todoktodok.presentation.compose.core.ObserveAsEvents
import com.team.todoktodok.presentation.compose.core.component.AlertSnackBar
import com.team.todoktodok.presentation.compose.core.component.CloverProgressBar
import com.team.todoktodok.presentation.compose.discussion.component.DiscussionTab
import com.team.todoktodok.presentation.compose.discussion.component.DiscussionToolbar
import com.team.todoktodok.presentation.compose.discussion.component.SearchDiscussionBar
import com.team.todoktodok.presentation.compose.discussion.model.Destination
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionsUiEvent
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionsUiState
import com.team.todoktodok.presentation.compose.discussion.vm.DiscussionsViewModel
import com.team.todoktodok.presentation.compose.discussion.vm.DiscussionsViewModelFactory
import com.team.todoktodok.presentation.compose.theme.White
import com.team.todoktodok.presentation.core.ExceptionMessageConverter
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscussionsScreen(
    exceptionMessageConverter: ExceptionMessageConverter,
    modifier: Modifier = Modifier,
    timeoutMillis: Long = 1500L,
    viewModel: DiscussionsViewModel =
        viewModel(
            factory = DiscussionsViewModelFactory((LocalContext.current.applicationContext as App).container),
        ),
) {
    val pagerState =
        rememberPagerState(initialPage = Destination.HOT.ordinal) { Destination.entries.size }
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    var snackbarJob by remember { mutableStateOf<Job?>(null) }

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

    var lastBackPressed by rememberSaveable { mutableLongStateOf(0L) }

    BackHandler(enabled = true) {
        val handled =
            viewModel.onBackPressed(timeoutMillis = 1500L, lastBackPressed = lastBackPressed)
        if (!handled) {
            lastBackPressed = System.currentTimeMillis()
            showMessage(context.getString(R.string.press_back_again_to_exit), 1500L)
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
        viewModel.loadMyDiscussions()
        viewModel.loadHotDiscussions()
    }

    DiscussionsScreen(
        exceptionMessageConverter = exceptionMessageConverter,
        isLoading = isLoading.value,
        uiState = uiState.value,
        pagerState = pagerState,
        snackbarHostState = snackbarHostState,
        onDiscussionClick = {},
        onTabChanged = viewModel::modifySearchKeyword,
        onSearchKeywordChanged = viewModel::modifySearchKeyword,
        onSearch = viewModel::loadSearchedDiscussions,
        onActivatedDiscussionLoadMore = viewModel::loadActivatedDiscussions,
        modifier = modifier,
    )
}

@Composable
private fun DiscussionsScreen(
    exceptionMessageConverter: ExceptionMessageConverter,
    isLoading: Boolean,
    uiState: DiscussionsUiState,
    pagerState: PagerState,
    snackbarHostState: SnackbarHostState,
    onDiscussionClick: (Long) -> Unit,
    onSearchKeywordChanged: (String) -> Unit,
    onTabChanged: (String) -> Unit,
    onSearch: () -> Unit,
    onActivatedDiscussionLoadMore: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            DiscussionToolbar(
                isExistNotification = uiState.isUnreadNotification,
                modifier =
                    modifier
                        .fillMaxWidth()
                        .statusBarsPadding(),
            )
        },
    ) { innerPadding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
        ) {
            DiscussionsContent(
                exceptionMessageConverter,
                uiState = uiState,
                pagerState = pagerState,
                onSearchKeywordChanged = { onSearchKeywordChanged(it) },
                onActivatedDiscussionLoadMore = onActivatedDiscussionLoadMore,
                onTabChanged = { tab -> if (tab != Destination.ALL) onTabChanged("") },
                onDiscussionClick = { onDiscussionClick(it) },
                onSearch = onSearch,
                modifier = Modifier.fillMaxSize(),
            )

            CloverProgressBar(isLoading)

            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { AlertSnackBar(snackbarData = it) },
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}

@Composable
fun DiscussionsContent(
    exceptionMessageConverter: ExceptionMessageConverter,
    uiState: DiscussionsUiState,
    pagerState: PagerState,
    onSearchKeywordChanged: (String) -> Unit,
    onActivatedDiscussionLoadMore: () -> Unit,
    onTabChanged: (Destination) -> Unit,
    onDiscussionClick: (Long) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .background(color = White),
    ) {
        SearchDiscussionBar(
            onSearch = onSearch,
            searchKeyword = uiState.searchDiscussion.type.keyword,
            previousKeyword = uiState.searchDiscussion.previousKeyword,
            onKeywordChange = { onSearchKeywordChanged(it) },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
        )

        DiscussionTab(
            messageConverter = exceptionMessageConverter,
            uiState = uiState,
            pagerState = pagerState,
            onActivatedDiscussionLoadMore = { onActivatedDiscussionLoadMore() },
            onClickDiscussion = onDiscussionClick,
            onTabChanged = onTabChanged,
        )
    }
}
