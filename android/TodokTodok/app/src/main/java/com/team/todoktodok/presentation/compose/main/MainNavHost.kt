package com.team.todoktodok.presentation.compose.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.team.todoktodok.presentation.compose.discussion.DiscussionsScreen
import com.team.todoktodok.presentation.compose.my.MyScreen

@Composable
fun MainNavHost(
    navController: NavHostController,
    startDestination: MainDestination,
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
                    MainDestination.Discussion -> DiscussionsScreen()
                    MainDestination.My -> MyScreen()
                }
            }
        }
    }
}
