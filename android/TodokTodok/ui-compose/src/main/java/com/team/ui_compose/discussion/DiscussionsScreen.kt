package com.team.ui_compose.discussion

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.core.navigation.DiscussionDetailRoute
import com.team.core.navigation.NotificationRoute
import com.team.core.serialization.SerializationDiscussion
import com.team.ui_compose.discussion.all.AllDiscussionsScreen
import com.team.ui_compose.discussion.component.DiscussionToolbar
import com.team.ui_compose.discussion.hot.HotDiscussionScreen
import com.team.ui_compose.discussion.model.DiscussionTabStatus
import com.team.ui_compose.main.MainUiState
import com.team.ui_compose.theme.Green1A
import com.team.ui_compose.theme.Pretendard
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscussionsScreen(
    discussionDetailNavigation: DiscussionDetailRoute,
    notificationNavigation: NotificationRoute,
    mainUiState: MainUiState,
    pagerState: PagerState,
    onSearch: () -> Unit,
    onChangeSearchBarVisibility: () -> Unit,
    onChangeKeyword: (String) -> Unit,
    onChangeIsExistNotification: () -> Unit,
    onCompleteRemoveDiscussion: (Long) -> Unit,
    onCompleteModifyDiscussion: (SerializationDiscussion) -> Unit,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier) {
        DiscussionToolbar(
            isExistNotification = mainUiState.hasUnreadNotification,
            notificationNavigation = notificationNavigation,
            mainUiState = mainUiState,
            onSearch = onSearch,
            onChangeSearchBarVisibility = onChangeSearchBarVisibility,
            onChangeIsExistNotification = onChangeIsExistNotification,
            onKeywordChange = { onChangeKeyword(it) },
        )

        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                Box(
                    Modifier
                        .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                        .height(4.dp)
                        .padding(horizontal = 20.dp)
                        .background(Green1A, RoundedCornerShape(50)),
                )
            },
            divider = {},
            containerColor = White,
            contentColor = Color.Black,
            edgePadding = 0.dp,
        ) {
            DiscussionTabStatus.entries.forEachIndexed { index, tab ->
                Tab(
                    text = {
                        Text(
                            text = stringResource(tab.label),
                            fontFamily = Pretendard,
                            fontWeight = FontWeight.SemiBold,
                        )
                    },
                    selected = pagerState.currentPage == index,
                    modifier =
                        Modifier
                            .width(100.dp)
                            .height(50.dp),
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = Color.LightGray,
            thickness = 1.dp,
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(White),
                contentAlignment = Alignment.Center,
            ) {
                when (DiscussionTabStatus.DiscussionTabStatus(page)) {
                    DiscussionTabStatus.HOT -> HotDiscussionScreen(discussionDetailNavigation)
                    DiscussionTabStatus.ALL ->
                        AllDiscussionsScreen(
                            allDiscussionScreenMode = mainUiState.allDiscussionMode,
                            searchDiscussion = mainUiState.searchDiscussion,
                            onCompleteRemoveDiscussion = onCompleteRemoveDiscussion,
                            onCompleteModifyDiscussion = onCompleteModifyDiscussion,
                        )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
private fun DiscussionsScreenPreview() {
    val discussionNavigation =
        object : DiscussionDetailRoute {
            override fun navigateToDiscussionDetail(
                fromActivity: Activity,
                discussionId: Long,
            ) {
            }

            override fun navigateToDiscussionDetailForResult(
                fromActivity: Activity,
                discussionId: Long,
                resultLauncher: ActivityResultLauncher<Intent>,
            ) {
            }
        }
    val notificationNavigation =
        object : com.team.core.navigation.NotificationRoute {
            override fun navigateToNotification(fromActivity: Activity) {
            }

            override fun navigateToNotificationForResult(
                fromActivity: Activity,
                resultLauncher: ActivityResultLauncher<Intent>,
            ) {
            }
        }
    DiscussionsScreen(
        discussionDetailNavigation = discussionNavigation,
        notificationNavigation = notificationNavigation,
        mainUiState = MainUiState(),
        pagerState =
            rememberPagerState(initialPage = DiscussionTabStatus.HOT.ordinal) {
                DiscussionTabStatus.entries.size
            },
        onCompleteRemoveDiscussion = {},
        onCompleteModifyDiscussion = {},
        onSearch = {},
        onChangeSearchBarVisibility = {},
        onChangeIsExistNotification = {},
        onChangeKeyword = {},
    )
}
