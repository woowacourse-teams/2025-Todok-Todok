package com.team.todoktodok.presentation.compose.discussion.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.team.todoktodok.presentation.compose.component.DiscussionCard
import com.team.todoktodok.presentation.compose.preview.SearchDiscussionsUiStatePreviewParameterProvider
import com.team.todoktodok.presentation.compose.theme.Green1A
import com.team.todoktodok.presentation.compose.theme.Pretendard

@Composable
fun SearchDiscussionScreen(
    onClick: (Long) -> Unit,
    searchDiscussion: SearchDiscussionsUiState,
    modifier: Modifier = Modifier,
) {
    if (searchDiscussion.items.isEmpty()) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier =
                modifier
                    .fillMaxSize()
                    .padding(bottom = 130.dp),
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_mascort),
                contentDescription = null,
                modifier = Modifier.size(130.dp),
            )
            val fullText =
                stringResource(
                    R.string.discussion_no_search_title,
                    searchDiscussion.searchKeyword,
                )
            Text(
                text =
                    buildAnnotatedString {
                        val keyword = searchDiscussion.searchKeyword
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
    } else {
        Column(
            verticalArrangement = Arrangement.spacedBy(15.dp),
            modifier = modifier.padding(horizontal = 10.dp),
        ) {
            searchDiscussion.items.forEach { item ->
                DiscussionCard(
                    uiState = item,
                    discussionCardType = searchDiscussion.type,
                    onClick = { onClick(item.discussionId) },
                )
            }
        }
    }
}

@Preview
@Composable
fun SearchDiscussionScreenPreview(
    @PreviewParameter(SearchDiscussionsUiStatePreviewParameterProvider::class)
    searchDiscussion: SearchDiscussionsUiState,
) {
    SearchDiscussionScreen(
        onClick = {},
        searchDiscussion = searchDiscussion,
    )
}

@Preview(showBackground = true)
@Composable
fun EmptySearchDiscussionScreenPreview() {
    SearchDiscussionScreen(
        onClick = {},
        searchDiscussion = SearchDiscussionsUiState(searchKeyword = "토론"),
    )
}
