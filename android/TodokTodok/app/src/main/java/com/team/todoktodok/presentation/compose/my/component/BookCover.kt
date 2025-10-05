package com.team.todoktodok.presentation.compose.my.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.team.domain.model.Book

@Composable
fun BookCover(
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
        modifier = modifier,
        loading = { ShimmerPlaceholder() },
        error = { BookErrorIllustration() },
        contentScale = ContentScale.Crop,
    )
}

@Preview
@Composable
private fun BookCoverPreview() {
    val book =
        Book(
            id = 1L,
            title = "The Hitchhiker's Guide to the Galaxy",
            author = "Douglas Adams",
            image = "https://search2.kakaocdn.net/thumb/R120x174.q85/?fname=http%3A%2F%2Ft1.daumcdn.net%2Flbook%2Fimage%2F1467038",
        )
    BookCover(book)
}
