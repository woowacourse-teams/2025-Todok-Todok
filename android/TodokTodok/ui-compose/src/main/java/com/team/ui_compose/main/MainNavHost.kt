package com.team.ui_compose.main

import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.team.core.navigation.DiscussionDetailRoute
import com.team.core.navigation.NotificationRoute
import com.team.core.navigation.SelectBookRoute
import com.team.core.navigation.SettingRoute
import com.team.core.serialization.SerializationDiscussion
import com.team.ui_compose.discussion.DiscussionsScreen
import com.team.ui_compose.my.MyScreen

@Composable
fun MainNavHost(
    discussionDetailNavigation: DiscussionDetailRoute,
    notificationNavigation: NotificationRoute,
    selectBookNavigation: SelectBookRoute,
    settingNavigation: SettingRoute,
    mainUiState: MainUiState,
    pagerState: PagerState,
    navController: NavHostController,
    onSearch: () -> Unit,
    onCompleteRemoveDiscussion: (Long) -> Unit,
    onCompleteModifyDiscussion: (SerializationDiscussion) -> Unit,
    onChangeSearchBarVisibility: () -> Unit,
    onChangeIsExistNotification: () -> Unit,
    onChangeKeyword: (String) -> Unit,
    navigateToDiscussion: () -> Unit,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = MainRoute.Discussion,
    ) {
        composable<MainRoute.Discussion> {
            DiscussionsScreen(
                discussionDetailNavigation = discussionDetailNavigation,
                notificationNavigation = notificationNavigation,
                mainUiState = mainUiState,
                pagerState = pagerState,
                onCompleteRemoveDiscussion = onCompleteRemoveDiscussion,
                onCompleteModifyDiscussion = onCompleteModifyDiscussion,
                onSearch = onSearch,
                onChangeSearchBarVisibility = onChangeSearchBarVisibility,
                onChangeIsExistNotification = onChangeIsExistNotification,
                onChangeKeyword = onChangeKeyword,
                modifier = modifier,
            )
        }

        composable<MainRoute.My> {
            MyScreen(
                discussionDetailNavigation = discussionDetailNavigation,
                selectBookNavigation = selectBookNavigation,
                settingNavigation = settingNavigation,
                navigateToDiscussion = navigateToDiscussion,
                modifier = modifier,
            )
        }
    }
}
