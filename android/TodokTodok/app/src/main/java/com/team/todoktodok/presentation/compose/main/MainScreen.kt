package com.team.todoktodok.presentation.compose.main

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.team.todoktodok.presentation.compose.discussion.component.DiscussionToolbar
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionTabStatus
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionTabStatus.Companion.DiscussionTabStatus
import com.team.todoktodok.presentation.xml.book.SelectBookActivity

@Composable
fun MainScreen(
    isUnreadNotification: Boolean,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val navController = rememberNavController()
    val startDestination = MainDestination.Discussion
    var selectedDestination by rememberSaveable { mutableStateOf(MainDestination.Discussion) }
    val pagerState =
        rememberPagerState(initialPage = DiscussionTabStatus.HOT.ordinal) { DiscussionTabStatus.entries.size }

    Scaffold(
        topBar = {
            DiscussionToolbar(
                tab = DiscussionTabStatus(pagerState.currentPage),
                isExistNotification = isUnreadNotification,
                onClickSearch = {},
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .statusBarsPadding(),
            )
        },
        bottomBar = {
            MainBottomNavigation(
                navController = navController,
                selectedDestination = selectedDestination.ordinal,
                onSelectedDestinationChanged = { index ->
                    selectedDestination = MainDestination.of(index)
                },
                onClickCreateDiscussion = {
                    context.startActivity(SelectBookActivity.Intent(context))
                },
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        MainNavHost(
            pagerState = pagerState,
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    MainScreen(isUnreadNotification = true)
}
