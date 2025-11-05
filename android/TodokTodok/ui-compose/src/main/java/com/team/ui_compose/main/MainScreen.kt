package com.team.ui_compose.main

import NotificationBottomSheet
import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.team.core.ExceptionMessageConverter
import com.team.core.navigation.DiscussionDetailRoute
import com.team.core.navigation.NotificationRoute
import com.team.core.navigation.SelectBookRoute
import com.team.core.navigation.SettingRoute
import com.team.core.serialization.SerializationDiscussion
import com.team.ui_compose.R
import com.team.ui_compose.common.LocalUiExceptionHandler
import com.team.ui_compose.common.ObserveAsEvents
import com.team.ui_compose.common.UiExceptionHandler
import com.team.ui_compose.component.AlertSnackBar
import com.team.ui_compose.discussion.model.DiscussionTabStatus
import com.team.ui_compose.discussion.model.DiscussionTabStatus.Companion.DiscussionTabStatus
import com.team.ui_compose.main.vm.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    discussionDetailNavigation: DiscussionDetailRoute,
    notificationNavigation: NotificationRoute,
    selectBookNavigation: SelectBookRoute,
    settingNavigation: SettingRoute,
    messageConverter: ExceptionMessageConverter,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel(),
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var showNotificationSheet by remember { mutableStateOf(false) }

    val requestPermissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { isGranted: Boolean ->
            if (isGranted) {
                viewModel.sendPushNotificationToken()
            }
        }

    fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS,
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                viewModel.sendPushNotificationToken()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            viewModel.sendPushNotificationToken()
        }
    }

    LaunchedEffect(uiState.value.isAllowed, uiState.value.isLoad) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            showNotificationSheet = uiState.value.isLoad && uiState.value.isAllowed
        }
    }

    if (showNotificationSheet) {
        ModalBottomSheet(
            onDismissRequest = { showNotificationSheet = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = Color.White,
        ) {
            NotificationBottomSheet(
                onCancel = {
                    viewModel.allowedNotification(!uiState.value.isAllowed)
                    showNotificationSheet = false
                },
                onConfirm = {
                    askNotificationPermission()
                    showNotificationSheet = false
                },
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
            pagerState = pagerState,
            discussionDetailNavigation = discussionDetailNavigation,
            notificationNavigation = notificationNavigation,
            selectBookNavigation = selectBookNavigation,
            settingNavigation = settingNavigation,
            onSearch = viewModel::loadSearchedDiscussions,
            onChangeSearchBarVisibility = viewModel::changeSearchBarVisibility,
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
    discussionDetailNavigation: DiscussionDetailRoute,
    notificationNavigation: NotificationRoute,
    selectBookNavigation: SelectBookRoute,
    settingNavigation: SettingRoute,
    uiState: MainUiState,
    pagerState: PagerState,
    onSearch: () -> Unit,
    onChangeSearchBarVisibility: () -> Unit,
    onChangeKeyword: (String) -> Unit,
    onChangeIsExistNotification: () -> Unit,
    onCompleteRemoveDiscussion: (Long) -> Unit,
    onCompleteModifyDiscussion: (SerializationDiscussion) -> Unit,
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            MainBottomNavigation(navController = navController)
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
            discussionDetailNavigation = discussionDetailNavigation,
            notificationNavigation = notificationNavigation,
            selectBookNavigation = selectBookNavigation,
            settingNavigation = settingNavigation,
            mainUiState = uiState,
            pagerState = pagerState,
            navController = navController,
            onSearch = onSearch,
            onChangeKeyword = onChangeKeyword,
            onCompleteRemoveDiscussion = onCompleteRemoveDiscussion,
            onCompleteModifyDiscussion = onCompleteModifyDiscussion,
            onChangeSearchBarVisibility = onChangeSearchBarVisibility,
            onChangeIsExistNotification = onChangeIsExistNotification,
            navigateToDiscussion = { navController.navigate(MainRoute.Discussion) },
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
