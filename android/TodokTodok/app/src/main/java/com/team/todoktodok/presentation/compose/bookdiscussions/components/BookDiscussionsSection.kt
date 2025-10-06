package com.team.todoktodok.presentation.compose.bookdiscussions.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.todoktodok.presentation.compose.bookdiscussions.model.DiscussionItem
import com.team.todoktodok.presentation.compose.core.component.DiscussionBottom
import com.team.todoktodok.presentation.compose.core.component.DiscussionTop
import com.team.todoktodok.presentation.compose.theme.White
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun BookDiscussionsSection(
    discussionItems: ImmutableList<DiscussionItem>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier) {
        items(
            items = discussionItems.toList(),
        ) {
            DiscussionCard(it)
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
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier =
                Modifier
                    .background(White)
                    .padding(12.dp),
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
                viewCount = uiState.viewsCount.toString(),
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
    val discussionItems =
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
                viewsCount = 323,
                commentCount = 12,
            )
        }.toImmutableList()

    BookDiscussionsSection(discussionItems, Modifier.height(500.dp))
}
