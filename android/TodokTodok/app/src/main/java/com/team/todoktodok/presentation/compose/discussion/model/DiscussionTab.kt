package com.team.todoktodok.presentation.compose.discussion.model

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.todoktodok.presentation.compose.discussion.all.AllDiscussionsScreen
import com.team.todoktodok.presentation.compose.discussion.hot.HotDiscussionScreen
import com.team.todoktodok.presentation.compose.discussion.latest.vm.LatestDiscussionViewModel
import com.team.todoktodok.presentation.compose.discussion.model.Destination.Companion.Destination
import com.team.todoktodok.presentation.compose.discussion.my.MyDiscussionsScreen
import com.team.todoktodok.presentation.compose.theme.Green1A
import com.team.todoktodok.presentation.compose.theme.Pretendard
import com.team.todoktodok.presentation.compose.theme.White
import com.team.todoktodok.presentation.core.ExceptionMessageConverter
import com.team.todoktodok.presentation.xml.profile.UserProfileTab
import kotlinx.coroutines.launch

@Composable
fun DiscussionTab(
    latestDiscussionViewModel: LatestDiscussionViewModel,
    messageConverter: ExceptionMessageConverter,
    uiState: DiscussionsUiState,
    pagerState: PagerState,
    onActivatedDiscussionLoadMore: () -> Unit,
    onTabChanged: (Destination) -> Unit,
    onClickDiscussion: (Long) -> Unit,
    onClickMyDiscussionHeader: (UserProfileTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .collect { page -> onTabChanged(Destination(page)) }
    }

    Column(modifier = modifier.fillMaxSize()) {
        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                val currentTab = tabPositions[pagerState.currentPage]
                Box(
                    Modifier
                        .tabIndicatorOffset(currentTab)
                        .padding(horizontal = 20.dp)
                        .height(4.dp)
                        .background(
                            color = Green1A,
                            shape = RoundedCornerShape(50),
                        ),
                )
            },
            divider = {},
            containerColor = White,
            contentColor = Color.Black,
            edgePadding = 0.dp,
        ) {
            Destination.entries.forEachIndexed { index, tab ->
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
            modifier =
                Modifier
                    .fillMaxWidth(),
            color = Color.LightGray,
            thickness = 1.dp,
        )

        HorizontalPager(
            state = pagerState,
        ) { page ->
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(White),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                when (Destination(page)) {
                    Destination.HOT -> {
                        HotDiscussionScreen(
                            uiState = uiState.hotDiscussion,
                            onLoadMore = { onActivatedDiscussionLoadMore() },
                            onClick = { onClick(it) },
                            modifier = Modifier.fillMaxSize(),
                        )
                    }

                    Destination.ALL -> {
                        AllDiscussionsScreen(
                            latestDiscussionViewModel = latestDiscussionViewModel,
                            messageConverter = messageConverter,
                            uiState = uiState.allDiscussions,
                            onClickDiscussion = { onClick(it) },
                            modifier = Modifier.fillMaxSize(),
                        )
                    }

                    Destination.MY -> {
                        MyDiscussionsScreen(
                            uiState = uiState.myDiscussion,
                            onClick = { onClick(it) },
                            onClickHeader = { onClickMyDiscussionHeader(it) },
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun DiscussionTabPreview() {
    val pagerState = rememberPagerState(initialPage = 0) { 3 }
//    DiscussionTab(
//        pagerState = pagerState,
//        uiState = DiscussionsUiState(),
//        onLatestDiscussionLoadMore = {},
//        onActivatedDiscussionLoadMore = {},
//        onRefresh = {},
//        onClick = {},
//        onClickMyDiscussionHeader = {},
//    )
}
