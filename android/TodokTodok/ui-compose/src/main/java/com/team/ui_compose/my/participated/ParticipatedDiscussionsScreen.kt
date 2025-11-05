package com.team.ui_compose.my.participated

import android.app.Activity.RESULT_OK
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.team.core.navigation.DiscussionDetailRoute
import com.team.core.navigation.SelectBookRoute
import com.team.ui_compose.R
import com.team.ui_compose.component.DiscussionCard
import com.team.ui_compose.component.ResourceNotFoundContent
import com.team.ui_compose.preview.ParticipatedDiscussionPreviewParameterProvider
import com.team.ui_compose.theme.Green1A

@Composable
fun ParticipatedDiscussionsScreen(
    discussionDetailNavigation: DiscussionDetailRoute,
    selectBookNavigation: SelectBookRoute,
    uiState: ParticipatedDiscussionsUiState,
    onChangeShowMyDiscussion: (Boolean) -> Unit,
    onCompleteShowDiscussionDetail: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (uiState.isEmpty()) {
        EmptyParticipatedDiscussionsContent(selectBookNavigation, modifier)
    } else {
        ParticipatedDiscussionsContent(
            discussionDetailNavigation = discussionDetailNavigation,
            uiState = uiState,
            onChangeShowMyDiscussion = onChangeShowMyDiscussion,
            onCompleteShowDiscussionDetail = onCompleteShowDiscussionDetail,
            modifier = modifier,
        )
    }
}

@Composable
private fun EmptyParticipatedDiscussionsContent(
    selectBookNavigation: SelectBookRoute,
    modifier: Modifier = Modifier,
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    ResourceNotFoundContent(
        title = stringResource(R.string.all_not_has_participated_discussions_title),
        subtitle = stringResource(R.string.profile_not_has_created_discussion_subtitle),
        actionTitle = stringResource(R.string.profile_action_created_discussion),
        onActionClick = {
            selectBookNavigation.navigateToSelectBook(context as androidx.activity.ComponentActivity)
        },
        modifier = modifier,
    )
}

@Composable
private fun ParticipatedDiscussionsContent(
    discussionDetailNavigation: DiscussionDetailRoute,
    uiState: ParticipatedDiscussionsUiState,
    onChangeShowMyDiscussion: (Boolean) -> Unit,
    onCompleteShowDiscussionDetail: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(10.dp)) {
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = stringResource(R.string.profile_my_discussion_toggle),
                style = MaterialTheme.typography.titleMedium,
            )

            Spacer(modifier = Modifier.width(10.dp))
            Switch(
                checked = uiState.showMyDiscussion,
                onCheckedChange = { isShow -> onChangeShowMyDiscussion(isShow) },
                colors =
                    SwitchDefaults.colors(
                        checkedTrackColor = Green1A,
                        uncheckedTrackColor = White,
                    ),
            )
        }

        DiscussionCards(
            discussionDetailNavigation = discussionDetailNavigation,
            uiState = uiState,
            onCompleteShowDiscussionDetail = onCompleteShowDiscussionDetail,
        )
    }
}

@Composable
private fun DiscussionCards(
    discussionDetailNavigation: DiscussionDetailRoute,
    uiState: ParticipatedDiscussionsUiState,
    onCompleteShowDiscussionDetail: () -> Unit,
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

    val discussions = if (uiState.showMyDiscussion) uiState.myDiscussion else uiState.discussions

    discussions.forEach { discussion ->
        DiscussionCard(
            uiState = discussion,
            discussionCardType = uiState.type,
            modifier = Modifier.padding(top = 10.dp),
            onClick = { discussionId ->
                discussionDetailNavigation.navigateToDiscussionDetailForResult(
                    fromActivity = context as androidx.activity.ComponentActivity,
                    discussionId = discussionId,
                    resultLauncher = activityResultLauncher,
                )
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyParticipatedDiscussionsScreenPreview() {
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
    val selectBookNavigation =
        object : SelectBookRoute {
            override fun navigateToSelectBook(fromActivity: android.app.Activity) {
            }
        }
    ParticipatedDiscussionsScreen(
        discussionDetailNavigation = discussionNavigation,
        selectBookNavigation = selectBookNavigation,
        uiState = ParticipatedDiscussionsUiState(),
        onChangeShowMyDiscussion = {},
        onCompleteShowDiscussionDetail = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun ParticipatedDiscussionsScreenPreview(
    @PreviewParameter(ParticipatedDiscussionPreviewParameterProvider::class)
    uiState: ParticipatedDiscussionsUiState,
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
    val selectBookNavigation =
        object : SelectBookRoute {
            override fun navigateToSelectBook(fromActivity: android.app.Activity) {
            }
        }
    ParticipatedDiscussionsScreen(
        discussionDetailNavigation = discussionNavigation,
        selectBookNavigation = selectBookNavigation,
        uiState = uiState,
        onChangeShowMyDiscussion = {},
        onCompleteShowDiscussionDetail = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun MyParticipatedDiscussionsScreenPreview(
    @PreviewParameter(ParticipatedDiscussionPreviewParameterProvider::class)
    uiState: ParticipatedDiscussionsUiState,
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
    val selectBookNavigation =
        object : SelectBookRoute {
            override fun navigateToSelectBook(fromActivity: android.app.Activity) {
            }
        }
    ParticipatedDiscussionsScreen(
        discussionDetailNavigation = discussionNavigation,
        selectBookNavigation = selectBookNavigation,
        uiState = uiState.copy(showMyDiscussion = true),
        onChangeShowMyDiscussion = {},
        onCompleteShowDiscussionDetail = {},
    )
}
