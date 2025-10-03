package com.team.todoktodok.presentation.compose.discussion.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.todoktodok.R
import com.team.todoktodok.presentation.compose.core.extension.noRippleClickable
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionTabStatus
import com.team.todoktodok.presentation.compose.theme.Green1A
import com.team.todoktodok.presentation.compose.theme.White
import com.team.todoktodok.presentation.xml.notification.NotificationActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscussionToolbar(
    tab: DiscussionTabStatus,
    onClickSearch: () -> Unit,
    isExistNotification: Boolean,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier =
            modifier
                .background(color = White),
    ) {
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier =
                        Modifier
                            .fillMaxSize(),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.img_mascort),
                        tint = null,
                        contentDescription = null,
                    )

                    Icon(
                        painter = painterResource(R.drawable.img_app_name),
                        tint = null,
                        contentDescription = null,
                    )
                }
            },
            actions = {
                Row {
                    if (tab == DiscussionTabStatus.ALL) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(R.string.content_description_discussions_toolbar_search),
                            modifier = Modifier.noRippleClickable(onClick = { onClickSearch }),
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Box(
                        modifier = Modifier.padding(end = 10.dp),
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_notification),
                            contentDescription = stringResource(R.string.content_description_discussions_toolbar_notification),
                            modifier =
                                Modifier
                                    .noRippleClickable(
                                        onClick = {
                                            context.startActivity(NotificationActivity.Intent(context))
                                        },
                                    ),
                        )
                        if (isExistNotification) {
                            val contentDescription =
                                stringResource(R.string.content_description_discussions_toolbar_has_notification)
                            Box(
                                modifier =
                                    Modifier
                                        .size(8.dp)
                                        .background(color = Green1A, shape = CircleShape)
                                        .align(Alignment.TopEnd)
                                        .semantics {
                                            this.contentDescription = contentDescription
                                        },
                            )
                        }
                    }
                }
            },
            colors =
                TopAppBarDefaults.topAppBarColors(
                    containerColor = White,
                ),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun HotDiscussionToolbarPreview() {
    DiscussionToolbar(
        tab = DiscussionTabStatus.HOT,
        onClickSearch = {},
        isExistNotification = true,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun AllDiscussionToolbarPreview2() {
    DiscussionToolbar(
        tab = DiscussionTabStatus.ALL,
        onClickSearch = {},
        isExistNotification = false,
    )
}
