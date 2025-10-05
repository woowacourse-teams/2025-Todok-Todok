package com.team.todoktodok.presentation.compose.bookdiscussions

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable

@Serializable
data class BookDetailRoute(
    val bookId: Long,
)

@SuppressLint("ComposeViewModelInjection")
@Composable
fun BookDetailNavHost(
    startBookId: Long,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(navController, startDestination = BookDetailRoute(startBookId)) {
        composable<BookDetailRoute> { backStackEntry ->
            BookDetailEntry(backStackEntry, modifier)
        }
    }
}
