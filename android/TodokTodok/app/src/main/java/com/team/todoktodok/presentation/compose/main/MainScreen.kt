package com.team.todoktodok.presentation.compose.main

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.team.todoktodok.App
import com.team.todoktodok.presentation.compose.discussion.component.DiscussionToolbar
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionTabStatus
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionsUiState
import com.team.todoktodok.presentation.compose.discussion.vm.DiscussionsViewModel
import com.team.todoktodok.presentation.compose.discussion.vm.DiscussionsViewModelFactory

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: DiscussionsViewModel =
        viewModel(
            factory = DiscussionsViewModelFactory((LocalContext.current.applicationContext as App).container),
        ),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val navController = rememberNavController()

    val pagerState =
        rememberPagerState(initialPage = DiscussionTabStatus.HOT.ordinal) {
            DiscussionTabStatus.entries.size
        }

    LaunchedEffect(Unit) {
        snapshotFlow { uiState.value.discussionTab }
            .collect { tab ->
                val page =
                    when (tab) {
                        DiscussionTabStatus.HOT -> DiscussionTabStatus.HOT.ordinal
                        DiscussionTabStatus.ALL -> DiscussionTabStatus.ALL.ordinal
                    }
                if (pagerState.currentPage != page) {
                    pagerState.animateScrollToPage(page)
                }
            }
    }

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
