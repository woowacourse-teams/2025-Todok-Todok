package com.team.todoktodok.presentation.compose.discussion.search

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.todoktodok.R
import com.team.todoktodok.presentation.compose.core.component.DiscussionCard
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionResult
import com.team.todoktodok.presentation.compose.preview.SearchDiscussionsUiStatePreviewParameterProvider
import com.team.todoktodok.presentation.compose.theme.Pretendard
import com.team.todoktodok.presentation.xml.discussiondetail.DiscussionDetailActivity
import com.team.todoktodok.presentation.xml.serialization.SerializationDiscussion

@Composable
fun SearchDiscussionScreen(
    uiState: SearchDiscussionsUiState,
    onCompleteRemoveDiscussion: (Long) -> Unit,
    onCompleteModifyDiscussion: (SerializationDiscussion) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (uiState.discussions.isEmpty()) {
        EmptySearchResults(uiState)
    } else {
        SearchResultDiscussions(
            uiState = uiState,
            onCompleteRemoveDiscussion = onCompleteRemoveDiscussion,
            onCompleteModifyDiscussion = onCompleteModifyDiscussion,
            modifier = modifier,
        )
    }
}

@Composable
private fun EmptySearchResults(
    uiState: SearchDiscussionsUiState,
    modifier: Modifier = Modifier,
) {
    val format = stringResource(R.string.discussion_no_search_title)
    val guideMessage = uiState.formatNotFoundGuideMessage(format)

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

        Text(
            text = guideMessage,
            fontSize = 20.sp,
            fontFamily = Pretendard,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 10.dp),
        )
    }
}

@Composable
private fun SearchResultDiscussions(
    uiState: SearchDiscussionsUiState,
    onCompleteRemoveDiscussion: (Long) -> Unit,
    onCompleteModifyDiscussion: (SerializationDiscussion) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val activityResultLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.let { data ->
                    when (val result = DiscussionResult.fromIntent(data)) {
                        is DiscussionResult.Deleted -> onCompleteRemoveDiscussion(result.id)
                        is DiscussionResult.Watched -> onCompleteModifyDiscussion(result.discussion)
                        DiscussionResult.None -> Unit
                    }
                }
            }
        }

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
                onClick = {
                    activityResultLauncher.launch(
                        DiscussionDetailActivity.Intent(
                            context = context,
                            discussionId = it,
                        ),
                    )
                },
            )
        }

        item { Spacer(modifier = Modifier.height(5.dp)) }
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchDiscussionScreenPreview(
    @PreviewParameter(SearchDiscussionsUiStatePreviewParameterProvider::class)
    searchDiscussion: SearchDiscussionsUiState,
) {
    SearchDiscussionScreen(
        uiState = searchDiscussion,
        onCompleteRemoveDiscussion = {},
        onCompleteModifyDiscussion = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun EmptySearchDiscussionScreenPreview() {
    SearchDiscussionScreen(
        uiState = SearchDiscussionsUiState(),
        onCompleteRemoveDiscussion = {},
        onCompleteModifyDiscussion = {},
    )
}
