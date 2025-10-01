package com.team.todoktodok.presentation.compose.core.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.team.todoktodok.R
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionUiState
import com.team.todoktodok.presentation.compose.preview.DiscussionUiStatePreviewParameterProvider
import com.team.todoktodok.presentation.compose.theme.Gray75
import com.team.todoktodok.presentation.compose.theme.RedFF
import com.team.todoktodok.presentation.compose.theme.White
import com.team.todoktodok.presentation.xml.discussiondetail.DiscussionDetailActivity

sealed interface DiscussionCardType {
    data object Default : DiscussionCardType

    data class QueryHighlighting(
        val keyword: String,
    ) : DiscussionCardType

    data object WriterHidden : DiscussionCardType

    data object OpinionVisible : DiscussionCardType
}

@Composable
fun DiscussionCard(
    uiState: DiscussionUiState,
    discussionCardType: DiscussionCardType,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val cardWidth =
            if (discussionCardType is DiscussionCardType.OpinionVisible) {
                maxWidth * 0.95f
            } else {
                maxWidth
            }

        ElevatedCard(
            onClick = {
                context.startActivity(
                    DiscussionDetailActivity.Intent(
                        context = context,
                        discussionId = uiState.discussionId,
                    ),
                )
            },
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier.width(cardWidth),
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

                when (discussionCardType) {
                    DiscussionCardType.Default,
                    DiscussionCardType.WriterHidden,
                    -> {
                        Text(
                            text = uiState.discussionTitle,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }

                    is DiscussionCardType.QueryHighlighting -> {
                        val highlightedText =
                            highlightedText(
                                uiState.discussionTitle,
                                discussionCardType.keyword,
                                contextLength = 10,
                            )
                        Text(
                            text = highlightedText,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }

                    DiscussionCardType.OpinionVisible -> {
                        Column {
                            Text(
                                text = uiState.discussionTitle,
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                            Text(
                                text = uiState.discussionOpinion,
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 2,
                                minLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(top = 10.dp),
                            )
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                DiscussionBottom(
                    writerNickname = uiState.writerNickname,
                    writerImage = uiState.writerProfileImage,
                    isLikedByMe = uiState.isLikedByMe,
                    likeCount = uiState.likeCount,
                    viewCount = uiState.viewCount,
                    commentCount = uiState.commentCount,
                    writerHidden = discussionCardType is DiscussionCardType.WriterHidden,
                    modifier = Modifier.padding(top = 10.dp),
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
            modifier = Modifier.size(width = 40.dp, height = 60.dp),
        )

        Column(modifier = Modifier.weight(0.75f)) {
            Text(
                text = bookTitle,
                style = MaterialTheme.typography.titleMedium,
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
    val horizontalArrangement = if (writerHidden) Arrangement.End else Arrangement.SpaceBetween

    Row(
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(),
    ) {
        if (!writerHidden) {
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
                    placeholder = painterResource(id = R.drawable.img_mascort),
                    error = painterResource(id = R.drawable.img_mascort),
                    fallback = painterResource(id = R.drawable.img_mascort),
                    modifier =
                        Modifier
                            .clip(CircleShape)
                            .size(20.dp),
                )
                Text(
                    text = writerNickname,
                    style = MaterialTheme.typography.labelSmall,
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
                painter =
                    painterResource(
                        id = if (isLikedByMe) R.drawable.btn_heart_filled else R.drawable.btn_heart_empty,
                    ),
                contentDescription = null,
                tint = if (isLikedByMe) RedFF else Color.Unspecified,
            )
            Text(text = likeCount, fontSize = 12.sp)

            Icon(painter = painterResource(id = R.drawable.ic_views), contentDescription = null)
            Text(text = viewCount, fontSize = 12.sp)

            Icon(painter = painterResource(id = R.drawable.btn_comment), contentDescription = null)
            Text(text = commentCount, fontSize = 12.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DiscussionCardPreviewDefault(
    @PreviewParameter(DiscussionUiStatePreviewParameterProvider::class) uiState: List<DiscussionUiState>,
) {
    DiscussionCard(
        uiState = uiState.first(),
        discussionCardType = DiscussionCardType.Default,
    )
}

@Preview(showBackground = true)
@Composable
private fun DiscussionCardPreviewHighlighted(
    @PreviewParameter(DiscussionUiStatePreviewParameterProvider::class) uiState: List<DiscussionUiState>,
) {
    DiscussionCard(
        uiState = uiState.first(),
        discussionCardType = DiscussionCardType.QueryHighlighting("JPA"),
    )
}

@Preview(showBackground = true)
@Composable
private fun DiscussionCardPreviewWriterHidden(
    @PreviewParameter(DiscussionUiStatePreviewParameterProvider::class) uiState: List<DiscussionUiState>,
) {
    DiscussionCard(
        uiState = uiState.first(),
        discussionCardType = DiscussionCardType.WriterHidden,
    )
}

@Preview(showBackground = true)
@Composable
private fun DiscussionCardPreviewResizing(
    @PreviewParameter(DiscussionUiStatePreviewParameterProvider::class) uiState: List<DiscussionUiState>,
) {
    DiscussionCard(
        uiState = uiState.first(),
        discussionCardType = DiscussionCardType.OpinionVisible,
    )
}
