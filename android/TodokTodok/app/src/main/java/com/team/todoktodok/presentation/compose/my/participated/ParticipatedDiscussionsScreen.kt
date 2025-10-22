package com.team.todoktodok.presentation.compose.my.participated

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.team.todoktodok.R
import com.team.todoktodok.presentation.compose.core.component.DiscussionCard
import com.team.todoktodok.presentation.compose.core.component.ResourceNotFoundContent
import com.team.todoktodok.presentation.compose.preview.ParticipatedDiscussionPreviewParameterProvider
import com.team.todoktodok.presentation.compose.theme.Green1A
import com.team.todoktodok.presentation.compose.theme.White
import com.team.todoktodok.presentation.xml.book.SelectBookActivity
import com.team.todoktodok.presentation.xml.discussiondetail.DiscussionDetailActivity

@Composable
fun ParticipatedDiscussionsScreen(
    uiState: ParticipatedDiscussionsUiState,
    onChangeShowMyDiscussion: (Boolean) -> Unit,
    onCompleteShowDiscussionDetail: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (uiState.isEmpty()) {
        EmptyParticipatedDiscussionsContent()
    } else {
        ParticipatedDiscussionsContent(
            uiState = uiState,
            onChangeShowMyDiscussion = onChangeShowMyDiscussion,
            onCompleteShowDiscussionDetail = onCompleteShowDiscussionDetail,
            modifier = modifier,
        )
    }
}

@Composable
private fun EmptyParticipatedDiscussionsContent(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    ResourceNotFoundContent(
        title = stringResource(R.string.profile_not_has_participated_discussions_title),
        subtitle = stringResource(R.string.profile_not_has_created_discussion_subtitle),
        actionTitle = stringResource(R.string.profile_action_created_discussion),
        onActionClick = {
            context.startActivity(SelectBookActivity.Intent(context))
        },
        modifier = modifier,
    )
}

@Composable
private fun ParticipatedDiscussionsContent(
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
            uiState = uiState,
            onCompleteShowDiscussionDetail = onCompleteShowDiscussionDetail,
        )
    }
}

@Composable
private fun DiscussionCards(
    uiState: ParticipatedDiscussionsUiState,
    onCompleteShowDiscussionDetail: () -> Unit,
) {
    val activityResultLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                onCompleteShowDiscussionDetail()
            }
        }

    val context = LocalContext.current
    val discussions = if (uiState.showMyDiscussion) uiState.myDiscussion else uiState.discussions

    discussions.forEach { discussion ->
        DiscussionCard(
            uiState = discussion,
            discussionCardType = uiState.type,
            modifier = Modifier.padding(top = 10.dp),
            onClick = {
                activityResultLauncher.launch(
                    DiscussionDetailActivity.Intent(
                        context,
                        discussion.discussionId,
                    ),
                )
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyParticipatedDiscussionsScreenPreview() {
    ParticipatedDiscussionsScreen(
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
    ParticipatedDiscussionsScreen(
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
    ParticipatedDiscussionsScreen(
        uiState = uiState.copy(showMyDiscussion = true),
        onChangeShowMyDiscussion = {},
        onCompleteShowDiscussionDetail = {},
    )
}
