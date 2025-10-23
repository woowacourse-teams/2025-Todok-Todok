package com.team.todoktodok.presentation.compose.main

import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.team.todoktodok.presentation.compose.discussion.DiscussionsScreen
import com.team.todoktodok.presentation.compose.my.MyScreen
import com.team.todoktodok.presentation.xml.serialization.SerializationDiscussion

@Composable
fun MainNavHost(
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
        startDestination = MainDestination.Discussion,
    ) {
        composable<MainDestination.Discussion> {
            DiscussionsScreen(
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
        composable<MainDestination.My> {
            MyScreen(
                navigateToDiscussion = navigateToDiscussion,
                modifier = modifier,
            )
        }
    }
}
