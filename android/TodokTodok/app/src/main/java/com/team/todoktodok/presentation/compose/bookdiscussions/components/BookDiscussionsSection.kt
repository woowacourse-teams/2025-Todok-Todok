package com.team.todoktodok.presentation.compose.bookdiscussions.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.todoktodok.presentation.compose.bookdiscussions.model.BookDiscussionsSectionUiState
import com.team.todoktodok.presentation.compose.bookdiscussions.model.DiscussionItem
import com.team.todoktodok.presentation.compose.core.component.DiscussionBottom
import com.team.todoktodok.presentation.compose.core.component.DiscussionTop
import com.team.todoktodok.presentation.compose.theme.White
import kotlinx.collections.immutable.toImmutableList

@Composable
fun BookDiscussionsSection(
    bookDiscussionsSectionUiState: BookDiscussionsSectionUiState,
    loadMoreItems: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val threshold = 5

    val shouldLoadMore by remember(
        listState,
        bookDiscussionsSectionUiState.isPagingLoading,
        bookDiscussionsSectionUiState.discussionItems,
    ) {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val total = layoutInfo.totalItemsCount
            val last = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            last != -1 && last + threshold >= total && !bookDiscussionsSectionUiState.isPagingLoading
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) loadMoreItems()
    }
    LazyColumn(modifier) {
        items(
            items = bookDiscussionsSectionUiState.discussionItems,
        ) {
            DiscussionCard(it)
            Spacer(Modifier.height(20.dp))
        }
        item {
            if (bookDiscussionsSectionUiState.isPagingLoading) {
                Box(
                    modifier =
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
}

@Composable
fun DiscussionCard(
    uiState: DiscussionItem,
    modifier: Modifier = Modifier,
    onClick: (Long) -> Unit = {},
) {
    ElevatedCard(
        onClick = { onClick(uiState.discussionId) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier,
    ) {
        Column(
            modifier =
                Modifier
                    .background(White)
                    .padding(top = 20.dp, bottom = 12.dp, start = 12.dp, end = 12.dp),
        ) {
            DiscussionTop(
                bookTitle = uiState.bookTitle,
                bookAuthor = uiState.bookAuthor,
                bookImage = uiState.bookImage,
            )

            Spacer(Modifier.height(18.dp))

            Text(
                text = uiState.discussionTitle,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(Modifier.height(8.dp))

            DiscussionBottom(
                writerNickname = uiState.writerName,
                writerImage = uiState.writerProfile,
                isLikedByMe = uiState.isLikedByMe,
                likeCount = uiState.likeCount.toString(),
                viewCount = uiState.viewCount.toString(),
                commentCount = uiState.commentCount.toString(),
                writerHidden = false,
                modifier = Modifier.padding(top = 10.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BookDiscussionsSectionPreview() {
    val bookDiscussionsSectionUiState =
        BookDiscussionsSectionUiState(
            false,
            List(12) {
                DiscussionItem(
                    discussionId = it.toLong() + 1,
                    bookTitle = "오브젝트 - 코드로 이해하는 객체지향 설계",
                    bookAuthor = "조영호",
                    bookImage = "https://dummyimage.com/200x300/cccccc/000000.png&text=Clean+Code",
                    discussionTitle = "클린 코드 왜 함? 클린코드 진짜 싫음 으아악아악아가아아악",
                    writerProfile = "",
                    writerName = "토독",
                    isLikedByMe = true,
                    likeCount = 233,
                    viewCount = 323,
                    commentCount = 12,
                )
            }.toImmutableList(),
        )
    BookDiscussionsSection(bookDiscussionsSectionUiState, {}, Modifier.height(500.dp))
}
