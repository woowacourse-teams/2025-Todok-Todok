package com.team.todoktodok.presentation.compose.main

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.team.todoktodok.App
import com.team.todoktodok.presentation.compose.LocalUiExceptionHandler
import com.team.todoktodok.presentation.compose.UiExceptionHandler
import com.team.todoktodok.presentation.compose.core.ObserveAsEvents
import com.team.todoktodok.presentation.compose.core.component.AlertSnackBar
import com.team.todoktodok.presentation.compose.discussion.component.DiscussionToolbar
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionTabStatus
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionsUiEvent
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionsUiState
import com.team.todoktodok.presentation.compose.discussion.vm.DiscussionsViewModel
import com.team.todoktodok.presentation.compose.discussion.vm.DiscussionsViewModelFactory
import com.team.todoktodok.presentation.core.ExceptionMessageConverter
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    messageConverter: ExceptionMessageConverter,
    modifier: Modifier = Modifier,
    viewModel: DiscussionsViewModel =
        viewModel(
            factory = DiscussionsViewModelFactory((LocalContext.current.applicationContext as App).container),
        ),
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    val pagerState =
        rememberPagerState(initialPage = DiscussionTabStatus.HOT.ordinal) {
            DiscussionTabStatus.entries.size
        }

    ObserveAsEvents(viewModel.uiEvent) { event ->
        when (event) {
            DiscussionsUiEvent.ScrollToAllDiscussion -> {
                if (uiState.value.discussionTab == DiscussionTabStatus.HOT) {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(DiscussionTabStatus.ALL.ordinal)
                    }
                }
            }

            is DiscussionsUiEvent.ShowErrorMessage -> {
                snackbarHostState.showSnackbar(
                    message = context.getString(messageConverter(event.exception)),
                )
            }
        }
    }

    CompositionLocalProvider(
        LocalUiExceptionHandler provides
            UiExceptionHandler(
                snackbarHostState = snackbarHostState,
                messageConverter = messageConverter,
            ),
    ) {
        MainScreenContent(
            uiState = uiState.value,
            navController = navController,
            pagerState = pagerState,
            onSearch = viewModel::loadSearchedDiscussions,
            onChangeSearchBarVisibility = viewModel::changeSearchBarVisibility,
            onChangeBottomNavigationTab = viewModel::changeBottomNavigationTab,
            onChangeKeyword = viewModel::modifySearchKeyword,
            modifier = modifier,
        )
    }
}

@Composable
fun MainScreenContent(
    uiState: DiscussionsUiState,
    navController: NavHostController,
    pagerState: PagerState,
    onSearch: () -> Unit,
    onChangeSearchBarVisibility: () -> Unit,
    onChangeBottomNavigationTab: (MainDestination) -> Unit,
    onChangeKeyword: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            DiscussionToolbar(
                isExistNotification = uiState.hasUnreadNotification,
                defaultDiscussionsUiState = uiState,
                onSearch = onSearch,
                onChangeSearchBarVisibility = onChangeSearchBarVisibility,
                onKeywordChange = { onChangeKeyword(it) },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .statusBarsPadding(),
            )
        },
        bottomBar = {
            MainBottomNavigation(
                navController = navController,
                selectedDestination = uiState.bottomNavigationTab,
                onSelectedDestinationChanged = {
                    onChangeBottomNavigationTab(it)
                },
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = LocalUiExceptionHandler.current.snackbarHostState,
                snackbar = { AlertSnackBar(snackbarData = it) },
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        MainNavHost(
            allDiscussionScreenMode = uiState.allDiscussionMode,
            searchDiscussionsUiState = uiState.searchDiscussion,
            pagerState = pagerState,
            navController = navController,
            startDestination = MainDestination.Discussion,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    val pagerState = rememberPagerState(initialPage = 0) { DiscussionTabStatus.entries.size }
    val navController = rememberNavController()

    MainScreenContent(
        uiState = DiscussionsUiState(),
        navController = navController,
        pagerState = pagerState,
        onSearch = {},
        onChangeBottomNavigationTab = {},
        onChangeKeyword = {},
        onChangeSearchBarVisibility = {},
    )
}
