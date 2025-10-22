package com.team.todoktodok.presentation.compose.my.books

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.google.common.collect.ImmutableList
import com.team.domain.model.Book
import com.team.todoktodok.R
import com.team.todoktodok.presentation.compose.bookdiscussions.BookDiscussionsActivity
import com.team.todoktodok.presentation.compose.core.component.ResourceNotFoundContent
import com.team.todoktodok.presentation.compose.my.component.BookCover
import com.team.todoktodok.presentation.compose.preview.MyBooksUiStatePreviewParameterProvider

@Composable
fun ActivatedBooksScreen(
    uiState: MyBooksUiModel,
    navigateToDiscussion: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (uiState.notHasBooks) {
        ActivatedBooksEmpty(navigateToDiscussion)
    } else {
        ActivatedBooksGrid(
            uiState = uiState,
            modifier = modifier,
        )
    }
}

@Composable
private fun ActivatedBooksEmpty(onClickAction: () -> Unit) {
    ResourceNotFoundContent(
        title = stringResource(R.string.profile_not_has_activated_book_title),
        subtitle = stringResource(R.string.profile_not_has_activated_book_subtitle),
        actionTitle = stringResource(R.string.profile_action_activated_book),
        onActionClick = { onClickAction },
    )
}

@Composable
private fun ActivatedBooksGrid(
    uiState: MyBooksUiModel,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier =
            modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp, vertical = 5.dp),
    ) {
        uiState.rows.forEach { rowBooks ->
            BooksRow(books = rowBooks)
        }
    }
}

@Composable
private fun BooksRow(
    books: ImmutableList<Book>,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(15.dp),
    ) {
        books.forEach { book ->
            BookCover(
                book,
                modifier =
                    Modifier
                        .weight(1f),
            ) { bookId ->
                context.startActivity(BookDiscussionsActivity.Intent(context, bookId))
            }
        }

        repeat(3 - books.size) {
            Box(modifier = Modifier.weight(1f))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ActivatedBooksScreenPreview(
    @PreviewParameter(MyBooksUiStatePreviewParameterProvider::class)
    uiState: MyBooksUiModel,
) {
    ActivatedBooksScreen(
        uiState = uiState,
        navigateToDiscussion = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun EmptyActivatedBooksScreenPreview() {
    ActivatedBooksScreen(
        uiState = MyBooksUiModel(),
        navigateToDiscussion = {},
    )
}
