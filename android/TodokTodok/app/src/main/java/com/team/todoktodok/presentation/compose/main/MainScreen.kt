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
import com.team.todoktodok.presentation.core.ExceptionMessageConverter

@Composable
fun MainScreen(
    messageConverter: ExceptionMessageConverter,
    isUnreadNotification: Boolean,
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val startDestination = MainDestination.Discussion
    var selectedDestination by rememberSaveable { mutableStateOf(MainDestination.Discussion) }

    Scaffold(
        topBar = {
            DiscussionToolbar(
                isExistNotification = isUnreadNotification,
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
        MainNavHost(
            messageConverter = messageConverter,
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    MainScreen(
        messageConverter = ExceptionMessageConverter(),
        isUnreadNotification = true,
        modifier = Modifier,
    )
}
