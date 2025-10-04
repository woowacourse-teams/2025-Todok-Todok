package com.team.todoktodok.presentation.compose.my.books

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.team.domain.model.Book
import com.team.todoktodok.App
import com.team.todoktodok.R
import com.team.todoktodok.presentation.compose.core.component.ResourceNotFoundContent
import com.team.todoktodok.presentation.compose.main.MainDestination
import com.team.todoktodok.presentation.compose.my.books.vm.MyBooksViewModel
import com.team.todoktodok.presentation.compose.my.books.vm.MyBooksViewModelFactory
import com.team.todoktodok.presentation.compose.my.component.BookErrorIllustration
import com.team.todoktodok.presentation.compose.my.component.ShimmerPlaceholder
import com.team.todoktodok.presentation.compose.preview.MyBooksUiStatePreviewParameterProvider
import com.team.todoktodok.presentation.compose.theme.GrayE0

@Composable
fun ActivatedBooksScreen(
    navController: NavHostController,
    onChangeBottomNavigationTab: (MainDestination) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MyBooksViewModel =
        viewModel(
            factory =
                MyBooksViewModelFactory(
                    (LocalContext.current.applicationContext as App).container,
                ),
        ),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadMyBooks()
    }

    ActivatedBooksScreen(
        uiState = uiState.value,
        navController = navController,
        onChangeBottomNavigationTab = onChangeBottomNavigationTab,
        modifier = modifier,
    )
}

@Composable
fun ActivatedBooksScreen(
    uiState: MyBooksUiState,
    onChangeBottomNavigationTab: (MainDestination) -> Unit,
    modifier: Modifier = Modifier,
    navController: NavHostController = NavHostController(LocalContext.current),
) {
    if (uiState.notHasBooks) {
        ResourceNotFoundContent(
            title = stringResource(R.string.profile_not_has_activated_book_title),
            subtitle = stringResource(R.string.profile_not_has_activated_book_subtitle),
            actionTitle = stringResource(R.string.profile_action_activated_book),
            onActionClick = {
                navController.navigate(MainDestination.Discussion.route) {
                    launchSingleTop = true
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    restoreState = true
                    onChangeBottomNavigationTab(MainDestination.Discussion)
                }
            },
        )
    } else {
        LazyVerticalGrid(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            columns = GridCells.Fixed(3),
            modifier =
                modifier
                    .fillMaxSize()
                    .padding(horizontal = 15.dp, vertical = 5.dp),
        ) {
            items(uiState.books, key = { it.id }) { book ->
                BookCover(book)
            }
        }
    }
}

@Composable
private fun BookCover(
    book: Book,
    modifier: Modifier = Modifier,
) {
    SubcomposeAsyncImage(
        model =
            ImageRequest
                .Builder(LocalContext.current)
                .data(book.image)
                .crossfade(true)
                .build(),
        contentDescription = book.title,
        modifier =
            modifier
                .fillMaxSize()
                .border(width = 1.dp, color = GrayE0)
                .aspectRatio(0.75f),
        loading = { ShimmerPlaceholder() },
        error = { BookErrorIllustration() },
        contentScale = ContentScale.Crop,
    )
}

@Preview(showBackground = true)
@Composable
private fun ActivatedBooksScreenPreview(
    @PreviewParameter(MyBooksUiStatePreviewParameterProvider::class)
    uiState: MyBooksUiState,
) {
    ActivatedBooksScreen(
        navController = NavHostController(LocalContext.current),
        onChangeBottomNavigationTab = {},
        uiState = uiState,
    )
}

@Preview(showBackground = true)
@Composable
private fun EmptyActivatedBooksScreenPreview() {
    ActivatedBooksScreen(
        navController = NavHostController(LocalContext.current),
        onChangeBottomNavigationTab = {},
        uiState = MyBooksUiState(),
    )
}
