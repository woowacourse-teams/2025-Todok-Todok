package com.team.todoktodok.presentation.compose.discussion

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.todoktodok.presentation.compose.LocalUiExceptionHandler
import com.team.todoktodok.presentation.compose.UiExceptionHandler
import com.team.todoktodok.presentation.compose.core.component.AlertSnackBar
import com.team.todoktodok.presentation.compose.core.component.DiscussionCardType
import com.team.todoktodok.presentation.compose.discussion.all.AllDiscussionsScreen
import com.team.todoktodok.presentation.compose.discussion.hot.HotDiscussionScreen
import com.team.todoktodok.presentation.compose.discussion.model.AllDiscussionMode
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionTabStatus
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionTabStatus.Companion.DiscussionTabStatus
import com.team.todoktodok.presentation.compose.discussion.search.SearchDiscussionsUiState
import com.team.todoktodok.presentation.compose.theme.Green1A
import com.team.todoktodok.presentation.compose.theme.Pretendard
import com.team.todoktodok.presentation.compose.theme.White
import com.team.todoktodok.presentation.core.ExceptionMessageConverter
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscussionsScreen(
    allDiscussionScreenMode: AllDiscussionMode,
    searchDiscussionsUiState: SearchDiscussionsUiState,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    CompositionLocalProvider(
        LocalUiExceptionHandler provides
            UiExceptionHandler(
                snackbarHostState = snackbarHostState,
                messageConverter = ExceptionMessageConverter(),
            ),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.fillMaxSize(),
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
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
                        when (DiscussionTabStatus(page)) {
                            DiscussionTabStatus.HOT -> HotDiscussionScreen()
                            DiscussionTabStatus.ALL ->
                                AllDiscussionsScreen(
                                    allDiscussionScreenMode = allDiscussionScreenMode,
                                    searchDiscussion = searchDiscussionsUiState,
                                )
                        }
                    }
                }
            }

            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { AlertSnackBar(snackbarData = it) },
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun DiscussionsScreenPreview() {
    DiscussionsScreen(
        allDiscussionScreenMode = AllDiscussionMode.LATEST,
        searchDiscussionsUiState =
            SearchDiscussionsUiState(
                discussions = emptyList(),
                type = DiscussionCardType.QueryHighlighting(""),
                previousKeyword = "",
            ),
        pagerState = PagerState { 1 },
    )
}
