package com.team.todoktodok.presentation.compose.discussion.my

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.todoktodok.R
import com.team.todoktodok.presentation.compose.core.component.ResourceNotFoundView
import com.team.todoktodok.presentation.compose.core.extension.noRippleClickable
import com.team.todoktodok.presentation.compose.discussion.created.CreatedDiscussionScreen
import com.team.todoktodok.presentation.compose.discussion.participated.ParticipatedDiscussionsScreen
import com.team.todoktodok.presentation.compose.preview.MyDiscussionsUiStatePreviewParameterProvider
import com.team.todoktodok.presentation.compose.theme.GrayE0
import com.team.todoktodok.presentation.compose.theme.White
import com.team.todoktodok.presentation.xml.profile.UserProfileTab

@Composable
fun MyDiscussionsScreen(
    uiState: MyDiscussionUiState,
    onClickHeader: (UserProfileTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (uiState.isEmpty()) {
        ResourceNotFoundView(
            title = stringResource(R.string.profile_not_has_created_discussion_title),
            subtitle = stringResource(R.string.profile_not_has_created_discussion_subtitle),
            modifier = Modifier.padding(top = 100.dp),
        )
        return
    }

    val participated = uiState.participatedDiscussionsUiState
    val created = uiState.createdDiscussionsUiState

    LazyColumn(modifier = modifier) {
        if (!participated.isEmpty()) {
            item {
                MyDiscussionHeader(
                    titleResourceId = R.string.profile_participated_discussion_room,
                    onClickHeader = { onClickHeader(UserProfileTab.PARTICIPATED_DISCUSSIONS) },
                )
            }
            item {
                ParticipatedDiscussionsScreen(
                    uiState = participated,
                )
            }
        }

        if (!participated.isEmpty() && !created.isEmpty()) {
            item {
                HorizontalDivider(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                    thickness = 3.dp,
                    color = GrayE0,
                )
            }
        }

        if (!created.isEmpty()) {
            item {
                MyDiscussionHeader(
                    titleResourceId = R.string.profile_created_discussion_room,
                    onClickHeader = { onClickHeader(UserProfileTab.CREATED_DISCUSSIONS) },
                )
            }
            item {
                CreatedDiscussionScreen(
                    uiState = created,
                )
            }
        }
    }
}

@Composable
fun MyDiscussionHeader(
    @StringRes titleResourceId: Int,
    onClickHeader: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier =
            modifier
                .padding(horizontal = 20.dp)
                .padding(top = 20.dp)
                .fillMaxWidth()
                .noRippleClickable(onClick = onClickHeader),
    ) {
        Text(
            text = stringResource(titleResourceId),
            fontSize = 18.sp,
            style = MaterialTheme.typography.titleMedium,
        )

        Image(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            modifier =
                Modifier
                    .size(15.dp)
                    .background(color = White),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MyDiscussionsScreenPreview(
    @PreviewParameter(MyDiscussionsUiStatePreviewParameterProvider::class)
    uiState: MyDiscussionUiState,
) {
    MyDiscussionsScreen(
        onClickHeader = {},
        uiState = uiState,
    )
}

@Preview(showBackground = true)
@Composable
private fun EmptyMyDiscussionsScreenPreview() {
    MyDiscussionsScreen(
        onClickHeader = {},
        uiState = MyDiscussionUiState(),
    )
}
