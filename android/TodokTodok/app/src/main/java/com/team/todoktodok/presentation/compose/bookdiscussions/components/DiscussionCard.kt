package com.team.todoktodok.presentation.compose.bookdiscussions.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.team.todoktodok.presentation.compose.bookdiscussions.model.DiscussionItem
import com.team.todoktodok.presentation.compose.core.component.DiscussionBottom
import com.team.todoktodok.presentation.compose.core.component.DiscussionTop
import com.team.todoktodok.presentation.compose.theme.White

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
