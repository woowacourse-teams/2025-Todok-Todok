package com.team.todoktodok.presentation.compose.bookdiscussions.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.todoktodok.presentation.compose.bookdiscussions.model.BookDetailSectionUiState
import com.team.todoktodok.presentation.compose.bookdiscussions.model.BookDiscussionsSectionUiState
import com.team.todoktodok.presentation.compose.bookdiscussions.model.DiscussionItem
import com.team.todoktodok.presentation.xml.discussiondetail.DiscussionDetailActivity
import kotlinx.collections.immutable.toImmutableList

@Composable
fun BookDiscussionsContent(
    bookDetailSectionUiState: BookDetailSectionUiState,
    bookDiscussionsSectionUiState: BookDiscussionsSectionUiState,
    loadMoreItems: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val listState = rememberLazyListState()
    val threshold = 2

    LaunchedEffect(
        listState,
        bookDiscussionsSectionUiState.isPagingLoading,
        bookDiscussionsSectionUiState.discussionItems.size,
    ) {
        snapshotFlow {
            val info = listState.layoutInfo
            val last = info.visibleItemsInfo.lastOrNull()?.index ?: -1
            val total = info.totalItemsCount
            last to total
        }.collect { (last, total) ->
            if (last >= total - 1 - threshold && !bookDiscussionsSectionUiState.isPagingLoading) {
                loadMoreItems()
            }
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier.padding(horizontal = 16.dp),
    ) {
        item {
            BookDetailSection(bookDetailSectionUiState, Modifier.fillMaxWidth())
            Spacer(Modifier.height(20.dp))
        }
        bookDiscussionsSectionItems(bookDiscussionsSectionUiState) {
            context.startActivity(DiscussionDetailActivity.Intent(context, it))
        }
    }
}

fun LazyListScope.bookDiscussionsSectionItems(
    uiState: BookDiscussionsSectionUiState,
    onActionClick: (Long) -> Unit = {},
) {
    items(
        items = uiState.discussionItems,
        key = { it.discussionId },
    ) { item ->
        DiscussionCard(item, Modifier, onActionClick)
        Spacer(Modifier.height(20.dp))
    }
    if (uiState.isPagingLoading) {
        item {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BookDiscussionsContentPreview() {
    val bookDetailSectionUiState =
        BookDetailSectionUiState(
            bookTitle = "오브젝트 - 코드로 이해하는 객체지향 설계",
            bookAuthor = "조영호",
            bookImage = "https://dummyimage.com/200x300/cccccc/000000.png&text=Clean+Code",
            bookPublisher = "동전",
            bookSummary = "책을 읽어야 합니다.책을 읽어야 합니다.책을 읽어야 합니다.책을 읽어야 합니다.책을 읽어야 합니다.책을 읽어야 합니다.책을 읽어야 합니다.책을 읽어야 합니다.책을 읽어야 합니다.",
        )
    val bookDiscussionsSectionUiState =
        BookDiscussionsSectionUiState(
            false,
            List(12) {
                DiscussionItem(
                    discussionId = it.toLong() + 1,
                    bookTitle = "오브젝트 - 코드로 이해하는 객체지향 설계",
                    bookAuthor = "조영호",
                    bookImage = "https://dummyimage.com/200x300/cccccc/000000.png&text=Clean+Code",
                    discussionTitle = "클린 코드 왜 함? 클린코드 진짜 싫음",
                    writerProfile = "",
                    writerName = "토독",
                    isLikedByMe = true,
                    likeCount = 233,
                    viewCount = 323,
                    commentCount = 12,
                )
            }.toImmutableList(),
        )
    BookDiscussionsContent(
        bookDetailSectionUiState,
        bookDiscussionsSectionUiState,
        {},
        Modifier.height(500.dp),
    )
}
