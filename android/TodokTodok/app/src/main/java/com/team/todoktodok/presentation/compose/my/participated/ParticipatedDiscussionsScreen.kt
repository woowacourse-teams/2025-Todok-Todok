package com.team.todoktodok.presentation.compose.my.participated

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
import com.team.todoktodok.presentation.compose.preview.ParticipatedDiscussionPreviewParameterProvider
import com.team.todoktodok.presentation.compose.theme.Green1A
import com.team.todoktodok.presentation.compose.theme.White
import com.team.todoktodok.presentation.xml.discussiondetail.DiscussionDetailActivity

@Composable
fun ParticipatedDiscussionsScreen(
    uiModel: ParticipatedDiscussionsUiModel,
    onChangeShowMyDiscussion: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (uiModel.)

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
                checked = uiModel.showMyDiscussion,
                onCheckedChange = { isShow -> onChangeShowMyDiscussion(isShow) },
                colors =
                    SwitchDefaults.colors(
                        checkedTrackColor = Green1A,
                        uncheckedTrackColor = White,
                    ),
            )
        }

        DiscussionCards(uiModel = uiModel)
    }
}

@Composable
private fun DiscussionCards(uiModel: ParticipatedDiscussionsUiModel) {
    val context = LocalContext.current
    uiModel.visibleDiscussions(uiModel.showMyDiscussion).forEach { discussion ->
        DiscussionCard(
            uiState = discussion,
            discussionCardType = uiModel.type,
            modifier = Modifier.padding(top = 10.dp),
            onClick = {
                context.startActivity(
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
private fun ParticipatedDiscussionsScreenPreview(
    @PreviewParameter(ParticipatedDiscussionPreviewParameterProvider::class)
    model: ParticipatedDiscussionsUiModel,
) {
    ParticipatedDiscussionsScreen(
        uiModel = model,
        onChangeShowMyDiscussion = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun MyParticipatedDiscussionsScreenPreview(
    @PreviewParameter(ParticipatedDiscussionPreviewParameterProvider::class)
    model: ParticipatedDiscussionsUiModel,
) {
    ParticipatedDiscussionsScreen(
        uiModel = model.copy(showMyDiscussion = true),
        onChangeShowMyDiscussion = {},
    )
}

