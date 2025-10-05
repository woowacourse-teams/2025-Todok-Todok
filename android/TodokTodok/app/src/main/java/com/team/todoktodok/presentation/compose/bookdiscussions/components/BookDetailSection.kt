package com.team.todoktodok.presentation.compose.bookdiscussions.components

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import com.team.todoktodok.R
import com.team.todoktodok.presentation.compose.bookdiscussions.model.BookDetailUiState

@Composable
fun BookDetailSection(
    bookDetailUiState: BookDetailUiState,
    modifier: Modifier = Modifier,
) {
    Row(modifier) {
        AsyncImage(
            model = bookDetailUiState,
            contentDescription = stringResource(R.string.book_image),
        )
    }
}

@Preview
@Composable
private fun BookDetailSectionPreview() {
}
