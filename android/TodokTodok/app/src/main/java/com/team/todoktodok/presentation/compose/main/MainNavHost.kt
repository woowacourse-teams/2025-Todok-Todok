package com.team.todoktodok.presentation.compose.main

import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.team.todoktodok.presentation.compose.discussion.DiscussionsScreen
import com.team.todoktodok.presentation.compose.discussion.model.AllDiscussionMode
import com.team.todoktodok.presentation.compose.discussion.search.SearchDiscussionsUiState
import com.team.todoktodok.presentation.compose.my.MyScreen
import com.team.todoktodok.presentation.xml.serialization.SerializationDiscussion

@Composable
fun MainNavHost(
    allDiscussionScreenMode: AllDiscussionMode,
    searchDiscussionsUiState: SearchDiscussionsUiState,
    pagerState: PagerState,
    navController: NavHostController,
    startDestination: MainDestination,
    onCompleteRemoveDiscussion: (Long) -> Unit,
    onCompleteModifyDiscussion: (SerializationDiscussion) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.route,
        modifier = modifier,
    ) {
        MainDestination.entries.forEach { destination ->
            composable(destination.route) {
                when (destination) {
                    MainDestination.Discussion ->
                        DiscussionsScreen(
                            searchDiscussionsUiState = searchDiscussionsUiState,
                            allDiscussionScreenMode = allDiscussionScreenMode,
                            onCompleteRemoveDiscussion = onCompleteRemoveDiscussion,
                            onCompleteModifyDiscussion = onCompleteModifyDiscussion,
                            pagerState = pagerState,
                        )

                    MainDestination.My -> MyScreen()
                }
            }
        }
    }
}
