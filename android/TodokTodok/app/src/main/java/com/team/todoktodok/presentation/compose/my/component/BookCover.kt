package com.team.todoktodok.presentation.compose.my.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.team.domain.model.Book
import com.team.todoktodok.presentation.compose.theme.Black21
import com.team.todoktodok.presentation.compose.theme.Gray66
import com.team.todoktodok.presentation.compose.theme.Pretendard

@Composable
fun BookCover(
    book: Book,
    modifier: Modifier = Modifier,
    onActionClick: (Long) -> Unit = {},
) {
    Column(
        modifier =
            modifier
                .aspectRatio(0.5f)
                .clickable {
                    onActionClick(book.id)
                },
    ) {
        SubcomposeAsyncImage(
            model =
                ImageRequest
                    .Builder(LocalContext.current)
                    .data(book.image)
                    .crossfade(true)
                    .build(),
            contentDescription = book.title,
            loading = { ShimmerPlaceholder() },
            error = { BookErrorIllustration() },
            contentScale = ContentScale.Crop,
            modifier =
                Modifier
                    .weight(7f)
                    .fillMaxWidth(),
        )

        Text(
            text = book.extractSubtitle(),
            maxLines = 2,
            minLines = 2,
            overflow = TextOverflow.Ellipsis,
            fontSize = 13.sp,
            color = Black21,
            fontFamily = Pretendard,
            fontWeight = FontWeight.Bold,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .heightIn(min = 10.dp),
        )

        Text(
            text = book.extractAuthor(),
            maxLines = 2,
            minLines = 2,
            overflow = TextOverflow.Ellipsis,
            fontSize = 11.sp,
            color = Gray66,
            fontFamily = Pretendard,
            fontWeight = FontWeight.SemiBold,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .weight(1f),
        )
    }
}

@Preview(showBackground = true)
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
