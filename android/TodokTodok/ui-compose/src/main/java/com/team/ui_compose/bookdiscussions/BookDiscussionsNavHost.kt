package com.team.ui_compose.bookdiscussions

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.team.core.ExceptionMessageConverter
import com.team.core.navigation.DiscussionDetailRoute
import com.team.ui_compose.common.LocalUiExceptionHandler
import com.team.ui_compose.common.UiExceptionHandler
import kotlinx.serialization.Serializable

@Serializable
data class BookDetailRoute(
    val bookId: Long,
)

@Composable
fun BookDetailNavHost(
    startBookId: Long,
    onNavigateToBack: () -> Unit,
    discussionDetailNavigation: DiscussionDetailRoute,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val exceptionHandler =
        remember(snackbarHostState) {
            UiExceptionHandler(
                snackbarHostState = snackbarHostState,
                messageConverter = ExceptionMessageConverter(),
            )
        }

    CompositionLocalProvider(LocalUiExceptionHandler provides exceptionHandler) {
        NavHost(navController, startDestination = BookDetailRoute(startBookId)) {
            composable<BookDetailRoute> { backStackEntry ->
                BookDetailEntry(
                    backStackEntry,
                    onNavigateToBack,
                    discussionDetailNavigation,
                    modifier,
                )
            }
        }
    }
}
