package com.team.todoktodok.presentation.compose.main

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.team.todoktodok.presentation.compose.discussion.component.DiscussionToolbar

@Composable
fun MainScreen(
    isUnreadNotification: Boolean,
    onClickNotification: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val startDestination = MainDestination.Discussion
    var selectedDestination by rememberSaveable { mutableStateOf(MainDestination.Discussion) }

    Scaffold(
        topBar = {
            DiscussionToolbar(
                isExistNotification = isUnreadNotification,
                onClickNotification = onClickNotification,
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
                onClickCreateDiscussion = {},
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        MainNavHost(navController, startDestination, Modifier.padding(innerPadding))
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    MainScreen(
        isUnreadNotification = true,
        onClickNotification = {},
        modifier = Modifier,
    )
}
