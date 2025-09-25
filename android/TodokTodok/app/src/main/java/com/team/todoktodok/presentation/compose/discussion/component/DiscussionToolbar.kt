package com.team.todoktodok.presentation.compose.discussion.component

import SearchDiscussionBar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.todoktodok.R
import com.team.todoktodok.presentation.compose.core.extension.noRippleClickable
import com.team.todoktodok.presentation.compose.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscussionToolbar(
    searchKeyword: String,
    onSearch: () -> Unit,
    onSearchKeywordChanged: (String) -> Unit,
    onClickNotification: () -> Unit,
    onClickProfile: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier.background(color = White),
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
                Icon(
                    painter = painterResource(R.drawable.ic_notification),
                    contentDescription = "notification",
                    Modifier
                        .background(color = White)
                        .padding(20.dp)
                        .noRippleClickable(onClick = { onClickNotification() })
                )
                Icon(
                    painter = painterResource(R.drawable.ic_profile),
                    contentDescription = "profile",
                    Modifier
                        .background(color = White)
                        .padding(end = 10.dp)
                        .noRippleClickable(onClick = { onClickProfile() }),
                )
            },
            colors =
                TopAppBarDefaults.topAppBarColors(
                    containerColor = White,
                ),
        )

        SearchDiscussionBar(
            onSearch = { onSearch() },
            searchKeyword = searchKeyword,
            onKeywordChange = { onSearchKeywordChanged(it) },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun DiscussionToolbarPreview() {
    DiscussionToolbar(
        onSearch = {},
        searchKeyword = "코틀린",
        onSearchKeywordChanged = {},
        onClickNotification = {},
        onClickProfile = {},
    )
}
