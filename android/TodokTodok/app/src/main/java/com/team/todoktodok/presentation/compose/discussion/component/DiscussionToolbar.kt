package com.team.todoktodok.presentation.compose.discussion.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.todoktodok.R
import com.team.todoktodok.presentation.compose.core.extension.noRippleClickable
import com.team.todoktodok.presentation.compose.main.MainUiState
import com.team.todoktodok.presentation.compose.theme.Green1A
import com.team.todoktodok.presentation.compose.theme.White
import com.team.todoktodok.presentation.xml.notification.NotificationActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscussionToolbar(
    mainUiState: MainUiState,
    onSearch: () -> Unit,
    onChangeSearchBarVisibility: () -> Unit,
    onKeywordChange: (String) -> Unit,
    isExistNotification: Boolean,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val density = LocalDensity.current

    Column(
        modifier =
            modifier
                .background(color = White),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(White)
                    .padding(horizontal = 10.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
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

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.content_description_discussions_toolbar_search),
                    modifier = Modifier.noRippleClickable { onChangeSearchBarVisibility() },
                )

                Spacer(modifier = Modifier.width(10.dp))

                Box(
                    modifier = Modifier.padding(end = 10.dp),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_notification),
                        contentDescription = stringResource(R.string.content_description_discussions_toolbar_notification),
                        modifier =
                            Modifier.noRippleClickable {
                                context.startActivity(
                                    NotificationActivity.Intent(context),
                                )
                            },
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
        }

        AnimatedVisibility(
            visible = mainUiState.searchBarVisible,
            enter =
                slideInVertically {
                    with(density) { -40.dp.roundToPx() }
                } +
                    expandVertically(expandFrom = Alignment.Top) +
                    fadeIn(initialAlpha = 0.3f),
            exit = slideOutVertically() + shrinkVertically() + fadeOut(),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp),
            ) {
                SearchDiscussionBar(
                    onSearch = onSearch,
                    searchKeyword = mainUiState.searchDiscussion.type.keyword,
                    onKeywordChange = { onKeywordChange(it) },
                    modifier = Modifier.fillMaxWidth(0.8f),
                )

                Text(
                    text = stringResource(R.string.discussions_toolbar_search_close),
                    color = Green1A,
                    style = MaterialTheme.typography.titleMedium,
                    modifier =
                        Modifier
                            .padding(20.dp)
                            .noRippleClickable { onChangeSearchBarVisibility() },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun DiscussionToolbarPreview() {
    DiscussionToolbar(
        mainUiState = MainUiState(),
        onSearch = {},
        onKeywordChange = {},
        onChangeSearchBarVisibility = {},
        isExistNotification = true,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun SearchBarExpandedToolbarPreview() {
    DiscussionToolbar(
        mainUiState = MainUiState(searchBarVisible = true),
        onSearch = {},
        onKeywordChange = {},
        onChangeSearchBarVisibility = {},
        isExistNotification = false,
    )
}
