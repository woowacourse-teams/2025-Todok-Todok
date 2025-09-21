package com.team.todoktodok.presentation.compose.discussion.latest

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.team.domain.model.Book
import com.team.domain.model.Discussion
import com.team.domain.model.member.Nickname
import com.team.domain.model.member.User
import com.team.todoktodok.presentation.compose.theme.Gray75
import com.team.todoktodok.presentation.compose.theme.Pretendard
import java.time.LocalDateTime

@Composable
fun DiscussionCard(
    discussion: Discussion,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        onClick = { onClick() },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        ),
        modifier = modifier
            .fillMaxWidth()
            .wrapContentSize()

    ) {
        Column(
            modifier = Modifier
                .padding(top = 19.dp, bottom = 10.dp)
                .padding(start = 12.dp, end = 15.dp)
        ) {
            Row {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(discussion.bookImage)
                        .crossfade(true)
                        .build(),
                    contentDescription = discussion.discussionTitle,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.clip(RoundedCornerShape(10.dp))
                )

                Column {
                    Text(
                        text = discussion.getBookTitle(),
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Text(
                        text = discussion.getBookAuthor(),
                        fontSize = 14.sp,
                        color = Gray75,
                        fontWeight = FontWeight.Light,
                    )
                }
            }

            Spacer(modifier = Modifier.height(19.dp))

            Text(
                text = discussion.discussionTitle,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            Column {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(discussion)
                        .crossfade(true)
                        .build(),
                    contentDescription = discussion.writerImage,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.clip(CircleShape),
                )

                Text(
                    text = discussion.writerNickname,
                    fontSize = 13.sp,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DiscussionCardPreview() {
    DiscussionCard(
        Discussion(
            id = 1L,
            discussionTitle = "JPA 성능 최적화",
            book = Book(1L, "자바 ORM 표준 JPA 프로그래밍", "김영한", ""),
            writer = User(1L, Nickname("홍길동"), ""),
            createAt = LocalDateTime.of(2025, 7, 12, 12, 0),
            discussionOpinion = "fetch join을 남발하면 안됩니다.",
            likeCount = 0,
            commentCount = 0,
            isLikedByMe = false,
        ), {}
    )
}
