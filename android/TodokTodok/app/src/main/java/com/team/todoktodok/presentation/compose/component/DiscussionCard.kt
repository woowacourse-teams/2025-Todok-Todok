package com.team.todoktodok.presentation.compose.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
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

sealed interface DiscussionCardType {
    data object Default : DiscussionCardType

    data object QueryHighlighting : DiscussionCardType

    data object WriterHidden : DiscussionCardType

    data object Resizing : DiscussionCardType
}

@Composable
fun DiscussionCard(
    uiState: DiscussionUiState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    discussionCardType: DiscussionCardType = DiscussionCardType.Default,
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxWidth(),
    ) {
        val screenWidth = maxWidth
        val cardWidth =
            when (discussionCardType) {
                is DiscussionCardType.Resizing -> screenWidth * 0.7f
                else -> screenWidth
            }

        ElevatedCard(
            onClick = onClick,
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
            modifier =
                Modifier
                    .width(cardWidth)
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

                when (discussionCardType) {
                    is DiscussionCardType.Default -> {
                        Text(
                            text = uiState.discussionTitle,
                            fontFamily = Pretendard,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(top = 5.dp),
                        )
                    }

                    is DiscussionCardType.QueryHighlighting -> {
                        val highlightedText =
                            highlightedText(
                                uiState.discussionTitle,
                                uiState.searchKeyword,
                                contextLength = 10,
                            )
                        Text(
                            text = highlightedText,
                            fontFamily = Pretendard,
                            fontWeight = SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(top = 5.dp),
                        )
                    }

                    is DiscussionCardType.WriterHidden -> {
                        Text(
                            text = uiState.discussionTitle,
                            fontFamily = Pretendard,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(top = 5.dp),
                        )
                    }

                    is DiscussionCardType.Resizing -> {
                        Text(
                            text = uiState.discussionTitle,
                            fontFamily = Pretendard,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(top = 5.dp),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                DiscussionBottom(
                    writerNickname = uiState.writerNickname,
                    writerImage = uiState.writerProfileImage,
                    isLikedByMe = uiState.isLikedByMe,
                    likeCount = uiState.likeCount,
                    viewCount = uiState.viewCount,
                    commentCount = uiState.commentCount,
                    writerHidden = discussionCardType is DiscussionCardType.WriterHidden,
                    modifier = Modifier.padding(top = 20.dp),
                )
            }
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
            modifier =
                Modifier
                    .height(60.dp)
                    .width(40.dp),
        )

        Column(modifier = Modifier.weight(0.75f)) {
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
    writerHidden: Boolean = false,
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
            if (!writerHidden) {
                AsyncImage(
                    model =
                        ImageRequest
                            .Builder(LocalContext.current)
                            .data(writerImage)
                            .crossfade(true)
                            .build(),
                    contentDescription = writerNickname,
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
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = if (isLikedByMe) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = null,
                tint = if (isLikedByMe) RedFF else Color.Unspecified,
            )
            Text(text = likeCount, fontSize = 12.sp)

            Icon(imageVector = Icons.Filled.Visibility, contentDescription = null)
            Text(text = viewCount, fontSize = 12.sp)

            Icon(imageVector = Icons.Outlined.ModeComment, contentDescription = null)
            Text(text = commentCount, fontSize = 12.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DiscussionCardPreviewDefault(
    @PreviewParameter(DiscussionUiStatePreviewParameterProvider::class) uiState: List<DiscussionUiState>,
) {
    DiscussionCard(
        uiState = uiState.first(),
        onClick = {},
        discussionCardType = DiscussionCardType.Default,
    )
}

@Preview(showBackground = true)
@Composable
fun DiscussionCardPreviewHighlighted(
    @PreviewParameter(DiscussionUiStatePreviewParameterProvider::class) uiState: List<DiscussionUiState>,
) {
    DiscussionCard(
        uiState = uiState.first(),
        onClick = {},
        discussionCardType = DiscussionCardType.QueryHighlighting,
    )
}

@Preview(showBackground = true)
@Composable
fun DiscussionCardPreviewWriterHidden(
    @PreviewParameter(DiscussionUiStatePreviewParameterProvider::class) uiState: List<DiscussionUiState>,
) {
    DiscussionCard(
        uiState = uiState.first(),
        onClick = {},
        discussionCardType = DiscussionCardType.WriterHidden,
    )
}

@Preview(showBackground = true)
@Composable
fun DiscussionCardPreviewResizing(
    @PreviewParameter(DiscussionUiStatePreviewParameterProvider::class) uiState: List<DiscussionUiState>,
) {
    DiscussionCard(
        uiState = uiState.first(),
        onClick = {},
        discussionCardType = DiscussionCardType.Resizing,
    )
}
