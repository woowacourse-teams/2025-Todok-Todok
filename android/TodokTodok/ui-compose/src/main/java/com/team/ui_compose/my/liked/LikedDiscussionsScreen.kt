package com.team.ui_compose.my.liked

import android.app.Activity.RESULT_OK
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.team.core.navigation.DiscussionDetailRoute
import com.team.ui_compose.R
import com.team.ui_compose.component.DiscussionCard
import com.team.ui_compose.component.ResourceNotFoundContent
import com.team.ui_compose.discussion.model.DiscussionUiModel
import com.team.ui_compose.preview.LikedDiscussionsUiStatePreviewParameterProvider

@Composable
fun LikedDiscussionsScreen(
    discussionDetailNavigation: DiscussionDetailRoute,
    uiState: LikedDiscussionsUiState,
    onCompleteShowDiscussionDetail: () -> Unit,
    navigateToDiscussion: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (uiState.discussions.isEmpty()) {
        EmptyLikedDiscussionsContent(
            onClickAction = navigateToDiscussion,
            modifier = modifier,
        )
    } else {
        LikedDiscussionsContent(
            discussionDetailNavigation = discussionDetailNavigation,
            uiState = uiState,
            onCompleteShowDiscussionDetail = onCompleteShowDiscussionDetail,
            modifier = modifier,
        )
    }
}

@Composable
private fun LikedDiscussionsContent(
    discussionDetailNavigation: DiscussionDetailRoute,
    uiState: LikedDiscussionsUiState,
    onCompleteShowDiscussionDetail: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = androidx.compose.ui.platform.LocalContext.current
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
                onClick = { discussionId ->
                    discussionDetailNavigation.navigateToDiscussionDetailForResult(
                        fromActivity = context as androidx.activity.ComponentActivity,
                        discussionId = discussionId,
                        resultLauncher = activityResultLauncher,
                    )
                },
                modifier = Modifier.padding(top = 10.dp),
            )
        }
    }
}

@Composable
private fun EmptyLikedDiscussionsContent(
    onClickAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ResourceNotFoundContent(
        title = stringResource(R.string.profile_not_has_liked_discussion_title),
        subtitle = stringResource(R.string.profile_not_has_liked_discussion_subtitle),
        actionTitle = stringResource(R.string.profile_action_activated_book),
        onActionClick = onClickAction,
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
private fun LikedDiscussionsScreen_HasData_Preview(
    @PreviewParameter(LikedDiscussionsUiStatePreviewParameterProvider::class)
    likedDiscussionsUiState: LikedDiscussionsUiState,
) {
    val discussionNavigation =
        object : DiscussionDetailRoute {
            override fun navigateToDiscussionDetail(
                fromActivity: android.app.Activity,
                discussionId: Long,
            ) {
            }

            override fun navigateToDiscussionDetailForResult(
                fromActivity: android.app.Activity,
                discussionId: Long,
                resultLauncher: androidx.activity.result.ActivityResultLauncher<android.content.Intent>,
            ) {
            }
        }
    LikedDiscussionsContent(
        discussionDetailNavigation = discussionNavigation,
        uiState = likedDiscussionsUiState,
        onCompleteShowDiscussionDetail = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun LikedDiscussionsScreen_Empty_Preview() {
    EmptyLikedDiscussionsContent(
        onClickAction = {},
    )
}
