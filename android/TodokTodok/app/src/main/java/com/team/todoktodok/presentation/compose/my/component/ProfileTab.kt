package com.team.todoktodok.presentation.compose.my.component

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.team.todoktodok.R
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionTabStatus
import com.team.todoktodok.presentation.compose.main.MainDestination
import com.team.todoktodok.presentation.compose.my.MyProfileUiState
import com.team.todoktodok.presentation.compose.my.books.ActivatedBooksScreen
import com.team.todoktodok.presentation.compose.my.liked.LikedDiscussionsScreen
import com.team.todoktodok.presentation.compose.my.participated.ParticipatedDiscussionsScreen
import com.team.todoktodok.presentation.compose.preview.MyProfileUiStatePreviewParameterProvider
import com.team.todoktodok.presentation.compose.theme.Green1A
import com.team.todoktodok.presentation.compose.theme.Pretendard
import com.team.todoktodok.presentation.compose.theme.White
import kotlinx.coroutines.launch

enum class ProfileTabDestination(
    @field:StringRes
    val label: Int,
    @field:StringRes
    val contentDescription: Int,
) {
    ACTIVATED_BOOKS(
        R.string.profile_active_books,
        R.string.content_description_profile_active_books,
    ),
    LIKED_DISCUSSIONS(R.string.profile_liked_room, R.string.content_description_profile_liked_room),
    PARTICIPATED_DISCUSSIONS(
        R.string.profile_participated_discussion_room,
        R.string.content_description_profile_participated_discussion_room,
    ),
}

@Composable
fun ProfileTab(
    uiState: MyProfileUiState,
    navController: NavHostController,
    onCompleteShowDiscussionDetail: () -> Unit,
    onChangeShowMyDiscussion: (Boolean) -> Unit,
    onChangeBottomNavigationTab: (MainDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pagerState =
        rememberPagerState(initialPage = DiscussionTabStatus.HOT.ordinal) {
            ProfileTabDestination.entries.size
        }

    Column(modifier = modifier) {
        ProfileTabRow(pagerState)
        ProfileTabPager(
            uiState = uiState,
            navController = navController,
            pagerState = pagerState,
            onChangeBottomNavigationTab = onChangeBottomNavigationTab,
            onCompleteShowDiscussionDetail = onCompleteShowDiscussionDetail,
            onChangeShowMyDiscussion = onChangeShowMyDiscussion,
        )
    }
}

@Composable
private fun ProfileTabRow(pagerState: PagerState) {
    val coroutineScope = rememberCoroutineScope()
    TabRow(
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
        containerColor = White,
        contentColor = Color.Black,
    ) {
        ProfileTabDestination.entries.forEachIndexed { index, tab ->
            Tab(
                text = {
                    Text(
                        text = stringResource(tab.label),
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.SemiBold,
                    )
                },
                selected = pagerState.currentPage == index,
                modifier = Modifier.height(50.dp),
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
            )
        }
    }
}

@Composable
private fun ProfileTabPager(
    uiState: MyProfileUiState,
    navController: NavHostController,
    pagerState: PagerState,
    onChangeBottomNavigationTab: (MainDestination) -> Unit,
    onChangeShowMyDiscussion: (Boolean) -> Unit,
    onCompleteShowDiscussionDetail: () -> Unit,
) {
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize(),
    ) { page ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(White),
        ) {
            when (ProfileTabDestination.entries[page]) {
                ProfileTabDestination.ACTIVATED_BOOKS ->
                    ActivatedBooksScreen(
                        uiState = uiState.activatedBooks,
                        navController = navController,
                        onChangeBottomNavigationTab = onChangeBottomNavigationTab,
                    )

                ProfileTabDestination.LIKED_DISCUSSIONS -> {
                    LikedDiscussionsScreen(
                        uiState = uiState.likedDiscussions,
                        onCompleteShowDiscussionDetail = onCompleteShowDiscussionDetail,
                    )
                }

                ProfileTabDestination.PARTICIPATED_DISCUSSIONS ->
                    ParticipatedDiscussionsScreen(
                        uiState = uiState.participatedDiscussions,
                        onChangeShowMyDiscussion = { onChangeShowMyDiscussion(it) },
                        onCompleteShowDiscussionDetail = onCompleteShowDiscussionDetail,
                    )
            }
        }
    }
}

@Preview
@Composable
private fun ProfileTabPreview(
    @PreviewParameter(MyProfileUiStatePreviewParameterProvider::class)
    uiState: MyProfileUiState,
) {
    ProfileTab(
        uiState = uiState,
        navController = NavHostController(LocalContext.current),
        onChangeBottomNavigationTab = {},
        onChangeShowMyDiscussion = {},
        onCompleteShowDiscussionDetail = {},
    )
}
