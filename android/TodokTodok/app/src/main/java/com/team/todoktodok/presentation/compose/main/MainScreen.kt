package com.team.todoktodok.presentation.compose.main

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.team.todoktodok.App
import com.team.todoktodok.R
import com.team.todoktodok.presentation.compose.LocalUiExceptionHandler
import com.team.todoktodok.presentation.compose.UiExceptionHandler
import com.team.todoktodok.presentation.compose.core.ObserveAsEvents
import com.team.todoktodok.presentation.compose.core.component.AlertSnackBar
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionTabStatus
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionTabStatus.Companion.DiscussionTabStatus
import com.team.todoktodok.presentation.compose.main.vm.MainViewModel
import com.team.todoktodok.presentation.compose.main.vm.MainViewModelFactory
import com.team.todoktodok.presentation.core.ExceptionMessageConverter
import com.team.todoktodok.presentation.xml.serialization.SerializationDiscussion
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    messageConverter: ExceptionMessageConverter,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel =
        viewModel(
            factory = MainViewModelFactory((LocalContext.current.applicationContext as App).container),
        ),
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    val pagerState =
        rememberPagerState(initialPage = DiscussionTabStatus.HOT.ordinal) {
            DiscussionTabStatus.entries.size
        }

    ObserveAsEvents(viewModel.uiEvent) { event ->
        when (event) {
            MainUiEvent.ScrollToAllDiscussion -> {
                if (DiscussionTabStatus(pagerState.currentPage) != DiscussionTabStatus.ALL) {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(DiscussionTabStatus.ALL.ordinal)
                    }
                }
            }

            is MainUiEvent.ShowErrorMessage -> {
                snackbarHostState.showSnackbar(
                    message = context.getString(messageConverter(event.exception)),
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadIsUnreadNotification()
    }

    CompositionLocalProvider(
        LocalUiExceptionHandler provides
            UiExceptionHandler(
                snackbarHostState = snackbarHostState,
                messageConverter = messageConverter,
            ),
    ) {
        BackPressToExit()
        MainScreenContent(
            uiState = uiState.value,
            pagerState = pagerState,
            onSearch = viewModel::loadSearchedDiscussions,
            onChangeSearchBarVisibility = viewModel::changeSearchBarVisibility,
            onChangeBottomNavigationTab = viewModel::changeBottomNavigationTab,
            onChangeKeyword = viewModel::modifySearchKeyword,
            onCompleteRemoveDiscussion = viewModel::removeDiscussion,
            onCompleteModifyDiscussion = viewModel::modifyDiscussion,
            modifier = modifier,
        )
    }
}

@Composable
fun MainScreenContent(
    uiState: MainUiState,
    pagerState: PagerState,
    onSearch: () -> Unit,
    onChangeSearchBarVisibility: () -> Unit,
    onChangeBottomNavigationTab: (MainDestination) -> Unit,
    onChangeKeyword: (String) -> Unit,
    onCompleteRemoveDiscussion: (Long) -> Unit,
    onCompleteModifyDiscussion: (SerializationDiscussion) -> Unit,
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    Scaffold(
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
            mainUiState = uiState,
            pagerState = pagerState,
            navController = navController,
            startDestination = MainDestination.Discussion,
            onSearch = onSearch,
            onChangeKeyword = onChangeKeyword,
            onCompleteRemoveDiscussion = onCompleteRemoveDiscussion,
            onCompleteModifyDiscussion = onCompleteModifyDiscussion,
            onChangeSearchBarVisibility = onChangeSearchBarVisibility,
            navigateToDiscussion = {
                navController.navigate(MainDestination.Discussion.route)
                onChangeBottomNavigationTab(MainDestination.Discussion)
            },
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
fun BackPressToExit() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val localUiExceptionHandler = LocalUiExceptionHandler.current
    var backPressedTime: Long = 0

    BackHandler(enabled = true) {
        if (System.currentTimeMillis() - backPressedTime < 2000) {
            (context as Activity).finish()
        } else {
            localUiExceptionHandler.showErrorMessage(
                scope = scope,
                message = context.getString(R.string.bottom_navigation_back_press),
            )
            backPressedTime = System.currentTimeMillis()
        }
    }
}
