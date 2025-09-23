package com.team.todoktodok.presentation.compose.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.ModeComment
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.team.todoktodok.presentation.compose.preview.DiscussionUiStatePreviewParameterProvider
import com.team.todoktodok.presentation.compose.theme.Gray75
import com.team.todoktodok.presentation.compose.theme.Pretendard
import com.team.todoktodok.presentation.compose.theme.RedFF
import com.team.todoktodok.presentation.compose.theme.White
import com.team.todoktodok.presentation.xml.discussions.DiscussionUiState

@Composable
fun DiscussionCard(
    uiState: DiscussionUiState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        modifier =
            modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
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

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = uiState.discussionTitle,
                fontFamily = Pretendard,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 5.dp),
            )

            Spacer(modifier = Modifier.height(8.dp))

            DiscussionBottom(
                writerNickname = uiState.writerNickname,
                writerImage = uiState.writerProfileImage,
                isLikedByMe = uiState.isLikedByMe,
                likeCount = uiState.likeCount,
                viewCount = uiState.viewCount,
                commentCount = uiState.commentCount,
                modifier = Modifier.padding(top = 20.dp),
            )
        }
    }
}

@Composable
private fun DiscussionTop(
    bookTitle: String,
    bookAuthor: String,
    bookImage: String,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier,
    ) {
        AsyncImage(
            model =
                ImageRequest
                    .Builder(LocalContext.current)
                    .data(bookImage)
                    .crossfade(true)
                    .build(),
            contentDescription = bookTitle,
            contentScale = ContentScale.Crop,
            modifier =
                Modifier
                    .height(60.dp)
                    .width(40.dp),
        )
        Column(
            modifier = Modifier.weight(0.75f),
        ) {
            Text(
                text = bookTitle,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 5.dp),
            )
            Text(
                text = bookAuthor,
                fontSize = 14.sp,
                color = Gray75,
                fontWeight = FontWeight.Light,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun DiscussionBottom(
    writerNickname: String,
    writerImage: String,
    isLikedByMe: Boolean,
    likeCount: String,
    viewCount: String,
    commentCount: String,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model =
                    ImageRequest
                        .Builder(LocalContext.current)
                        .data(writerImage)
                        .crossfade(true)
                        .build(),
                contentDescription = writerNickname,
                contentScale = ContentScale.Crop,
                modifier =
                    Modifier
                        .clip(CircleShape)
                        .height(20.dp)
                        .width(20.dp),
            )

            Text(
                text = writerNickname,
                fontSize = 13.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        DiscussionStats(
            isLikedByMe = isLikedByMe,
            likeCount = likeCount,
            viewCount = viewCount,
            commentCount = commentCount,
        )
    }
}

@Composable
private fun DiscussionStats(
    isLikedByMe: Boolean,
    likeCount: String,
    viewCount: String,
    commentCount: String,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DiscussionStat(
            content = likeCount,
            icon = {
                if (isLikedByMe) {
                    Icon(
                        imageVector = Icons.Outlined.Favorite,
                        contentDescription = null,
                        tint = RedFF,
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        contentDescription = null,
                    )
                }
            },
        )
        DiscussionStat(
            content = viewCount,
            icon = {
                Icon(
                    imageVector = Icons.Default.Visibility,
                    contentDescription = null,
                )
            },
        )
        DiscussionStat(
            content = commentCount,
            icon = {
                Icon(
                    imageVector = Icons.Outlined.ModeComment,
                    contentDescription = null,
                )
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DiscussionCardPreview(
    @PreviewParameter(DiscussionUiStatePreviewParameterProvider::class) uiState: List<DiscussionUiState>,
) {
    DiscussionCard(
        uiState.first(),
        {},
    )
}
