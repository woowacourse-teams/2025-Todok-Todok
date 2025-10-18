package com.team.todoktodok.presentation.compose.main

import NotificationBottomSheet
import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    messageConverter: ExceptionMessageConverter,
    askPermission: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel =
        viewModel(
            factory = MainViewModelFactory((LocalContext.current.applicationContext as App).container),
        ),
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    var showNotificationSheet by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED) {
            showNotificationSheet = true
        }
    }

    if (showNotificationSheet) {
        ModalBottomSheet(
            onDismissRequest = { showNotificationSheet = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        ) {
            NotificationBottomSheet(
                onCancel = { showNotificationSheet = false },
                onConfirm = {
                    askPermission()
                    showNotificationSheet = false
                }
            )
        }
    }

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
            navController = navController,
            pagerState = pagerState,
            onSearch = viewModel::loadSearchedDiscussions,
            onChangeSearchBarVisibility = viewModel::changeSearchBarVisibility,
            onChangeBottomNavigationTab = viewModel::changeBottomNavigationTab,
            onChangeKeyword = viewModel::modifySearchKeyword,
            onChangeIsExistNotification = viewModel::loadIsUnreadNotification,
            onCompleteRemoveDiscussion = viewModel::removeDiscussion,
            onCompleteModifyDiscussion = viewModel::modifyDiscussion,
            modifier = modifier,
        )
    }
}

@Composable
fun MainScreenContent(
    uiState: MainUiState,
    navController: NavHostController,
    pagerState: PagerState,
    onSearch: () -> Unit,
    onChangeSearchBarVisibility: () -> Unit,
    onChangeBottomNavigationTab: (MainDestination) -> Unit,
    onChangeKeyword: (String) -> Unit,
    onChangeIsExistNotification: () -> Unit,
    onCompleteRemoveDiscussion: (Long) -> Unit,
    onCompleteModifyDiscussion: (SerializationDiscussion) -> Unit,
    modifier: Modifier = Modifier,
) {
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
            onChangeBottomNavigationTab = onChangeBottomNavigationTab,
            onChangeIsExistNotification = onChangeIsExistNotification,
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
