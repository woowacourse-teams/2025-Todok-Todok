package com.team.todoktodok.presentation.compose.my.liked

import android.app.Activity.RESULT_OK
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.team.todoktodok.R
import com.team.todoktodok.presentation.compose.core.component.DiscussionCard
import com.team.todoktodok.presentation.compose.core.component.ResourceNotFoundContent
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionUiModel
import com.team.todoktodok.presentation.compose.preview.LikedDiscussionsUiStatePreviewParameterProvider
import com.team.todoktodok.presentation.xml.book.SelectBookActivity
import com.team.todoktodok.presentation.xml.discussiondetail.DiscussionDetailActivity

@Composable
fun LikedDiscussionsScreen(
    uiState: LikedDiscussionsUiState,
    onCompleteShowDiscussionDetail: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (uiState.discussions.isEmpty()) {
        EmptyLikedDiscussionsContent(modifier)
    } else {
        LikedDiscussionsContent(
            uiState = uiState,
            onCompleteShowDiscussionDetail = onCompleteShowDiscussionDetail,
            modifier = modifier,
        )
    }
}

@Composable
private fun LikedDiscussionsContent(
    uiState: LikedDiscussionsUiState,
    onCompleteShowDiscussionDetail: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val activityResultLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                onCompleteShowDiscussionDetail()
            }
        }

    Column(
        modifier = modifier.padding(10.dp),
    ) {
        uiState.discussions.forEach { item: DiscussionUiModel ->
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
                modifier = Modifier.padding(top = 10.dp),
            )
        }
    }
}

@Composable
private fun EmptyLikedDiscussionsContent(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    ResourceNotFoundContent(
        title = stringResource(R.string.profile_not_has_liked_discussion_title),
        subtitle = stringResource(R.string.profile_not_has_liked_discussion_subtitle),
        actionTitle = stringResource(R.string.profile_action_activated_book),
        onActionClick = {
            context.startActivity(SelectBookActivity.Intent(context))
        },
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
private fun LikedDiscussionsScreen_HasData_Preview(
    @PreviewParameter(LikedDiscussionsUiStatePreviewParameterProvider::class)
    likedDiscussionsUiState: LikedDiscussionsUiState,
) {
    LikedDiscussionsContent(
        uiState = likedDiscussionsUiState,
        onCompleteShowDiscussionDetail = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun LikedDiscussionsScreen_Empty_Preview() {
    EmptyLikedDiscussionsContent()
}
