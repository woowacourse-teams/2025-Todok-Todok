package com.team.todoktodok.presentation.compose.discussion.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.todoktodok.R
import com.team.todoktodok.presentation.compose.theme.GreenF0

@Composable
fun DiscussionFAB(
    onClickCreateDiscussion: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FloatingActionButton(
        onClick = { onClickCreateDiscussion() },
        backgroundColor = GreenF0,
        modifier = modifier,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_create_discussion),
            contentDescription = stringResource(R.string.all_create_discussion_room),
            tint = Color.Unspecified,
            modifier =
                Modifier
                    .padding(5.dp)
                    .size(60.dp),
        )
    }
}

@Preview
@Composable
private fun DiscussionFABPreview() {
    DiscussionFAB(onClickCreateDiscussion = {})
}
