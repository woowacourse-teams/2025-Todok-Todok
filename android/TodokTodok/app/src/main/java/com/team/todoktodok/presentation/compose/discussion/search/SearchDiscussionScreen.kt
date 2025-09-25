package com.team.todoktodok.presentation.compose.discussion.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.todoktodok.R
import com.team.todoktodok.presentation.compose.core.component.DiscussionCard
import com.team.todoktodok.presentation.compose.preview.SearchDiscussionsUiStatePreviewParameterProvider
import com.team.todoktodok.presentation.compose.theme.Green1A
import com.team.todoktodok.presentation.compose.theme.Pretendard

@Composable
fun SearchDiscussionScreen(
    uiState: SearchDiscussionsUiState,
    onClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (uiState.discussions.isEmpty()) {
        EmptySearchResults(uiState)
    } else {
        SearchResultDiscussions(
            uiState = uiState,
            onClick = onClick,
            modifier = modifier,
        )
    }
}

@Composable
fun EmptySearchResults(
    uiState: SearchDiscussionsUiState,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_mascort),
            contentDescription = null,
            modifier = Modifier.size(130.dp),
        )
        val fullText =
            stringResource(
                R.string.discussion_no_search_title,
                uiState.searchKeyword,
            )
        Text(
            text =
                buildAnnotatedString {
                    val keyword = uiState.searchKeyword
                    val startIndex = fullText.indexOf(keyword)
                    if (startIndex >= 0) {
                        append(fullText.substring(0, startIndex))
                        withStyle(SpanStyle(color = Green1A, fontWeight = FontWeight.Bold)) {
                            append(keyword)
                        }
                        append(fullText.substring(startIndex + keyword.length))
                    } else {
                        append(fullText)
                    }
                },
            fontSize = 20.sp,
            fontFamily = Pretendard,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 10.dp),
        )
    }
}

@Composable
fun SearchResultDiscussions(
    uiState: SearchDiscussionsUiState,
    onClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(15.dp),
        modifier = modifier.padding(horizontal = 10.dp),
    ) {
        item {
            Text(
                stringResource(R.string.discussion_search_result_count).format(uiState.discussions.size),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 15.dp),
            )
        }

        items(
            items = uiState.discussions,
            key = { it.discussionId },
        ) { item ->
            DiscussionCard(
                uiState = item,
                discussionCardType = uiState.type,
                onClick = { onClick(item.discussionId) },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchDiscussionScreenPreview(
    @PreviewParameter(SearchDiscussionsUiStatePreviewParameterProvider::class)
    searchDiscussion: SearchDiscussionsUiState,
) {
    SearchDiscussionScreen(
        onClick = {},
        uiState = searchDiscussion,
    )
}

@Preview(showBackground = true)
@Composable
fun EmptySearchDiscussionScreenPreview() {
    SearchDiscussionScreen(
        onClick = {},
        uiState = SearchDiscussionsUiState(searchKeyword = "토론"),
    )
}
