package com.team.ui_compose.my

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.core.navigation.DiscussionDetailRoute
import com.team.core.navigation.SelectBookRoute
import com.team.core.navigation.SettingRoute
import com.team.domain.model.ImagePayload
import com.team.ui_compose.common.LocalUiExceptionHandler
import com.team.ui_compose.common.ObserveAsEvents
import com.team.ui_compose.component.CloverProgressBar
import com.team.ui_compose.my.component.EditableProfileImage
import com.team.ui_compose.my.component.Information
import com.team.ui_compose.my.component.MyToolbar
import com.team.ui_compose.my.component.ProfileTab
import com.team.ui_compose.my.vm.MyProfileViewModel
import com.team.ui_compose.preview.MyProfileUiStatePreviewParameterProvider
import com.team.ui_compose.theme.TodoktodokTheme

@Composable
fun MyScreen(
    discussionDetailNavigation: DiscussionDetailRoute,
    selectBookNavigation: SelectBookRoute,
    settingNavigation: SettingRoute,
    navigateToDiscussion: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MyProfileViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val exceptionHandler = LocalUiExceptionHandler.current
    val coroutineScope = rememberCoroutineScope()

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadInitialProfile()
    }

    ObserveAsEvents(viewModel.uiEvent) { event ->
        when (event) {
            is MyProfileUiEvent.ShowErrorMessage -> {
                val message = context.getString(exceptionHandler.messageConverter(event.exception))
                exceptionHandler.showErrorMessage(coroutineScope, message)
            }
        }
    }

    MyScreen(
        discussionDetailNavigation = discussionDetailNavigation,
        selectBookNavigation = selectBookNavigation,
        settingNavigation = settingNavigation,
        uiState = uiState.value,
        isLoading = isLoading.value,
        navigateToDiscussion = navigateToDiscussion,
        onChangeShowMyDiscussion = viewModel::toggleShowMyDiscussion,
        onCompleteShowDiscussionDetail = viewModel::loadDiscussions,
        onImageSelected = viewModel::modifyProfileImage,
        onRefresh = { viewModel.loadInitialProfile() },
        modifier = modifier,
    )
}

@Composable
fun MyScreen(
    discussionDetailNavigation: DiscussionDetailRoute,
    selectBookNavigation: SelectBookRoute,
    settingNavigation: SettingRoute,
    uiState: MyProfileUiState,
    isLoading: Boolean,
    navigateToDiscussion: () -> Unit,
    onCompleteShowDiscussionDetail: () -> Unit,
    onChangeShowMyDiscussion: (Boolean) -> Unit,
    onImageSelected: (ImagePayload) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier =
            modifier
                .fillMaxSize()
                .background(color = White),
    ) {
        item {
            MyToolbar(
                settingNavigation = settingNavigation,
                onRefresh = onRefresh,
            )
        }

        item {
            EditableProfileImage(
                profileImageUrl = uiState.profile.profileImage,
                onImageSelected = onImageSelected,
            )
        }

        item {
            Information(
                nickname = uiState.profile.nickname,
                profileMessage = uiState.profile.message,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
            )
        }

        item {
            ProfileTab(
                discussionDetailNavigation = discussionDetailNavigation,
                selectBookNavigation = selectBookNavigation,
                uiState = uiState,
                navigateToDiscussion = navigateToDiscussion,
                onChangeShowMyDiscussion = { onChangeShowMyDiscussion(it) },
                onCompleteShowDiscussionDetail = onCompleteShowDiscussionDetail,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
            )
        }
    }

    if (isLoading) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CloverProgressBar(
                visible = true,
                size = 150.dp,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MyScreenPreview(
    @PreviewParameter(MyProfileUiStatePreviewParameterProvider::class)
    uiState: MyProfileUiState,
) {
    val discussionNavigation =
        object : DiscussionDetailRoute {
            override fun navigateToDiscussionDetail(
                fromActivity: android.app.Activity,
                discussionId: Long,
            ) {
            }

            override fun navigateToDiscussionDetailForResult(
                fromActivity: android.app.Activity,
                discussionId: Long,
                resultLauncher: androidx.activity.result.ActivityResultLauncher<android.content.Intent>,
            ) {
            }
        }
    val selectBookNavigation =
        object : SelectBookRoute {
            override fun navigateToSelectBook(fromActivity: android.app.Activity) {
            }
        }
    val settingNavigation =
        object : SettingRoute {
            override fun navigateToSetting(fromActivity: android.app.Activity) {
            }

            override fun navigateToSettingForResult(
                fromActivity: android.app.Activity,
                resultLauncher: androidx.activity.result.ActivityResultLauncher<android.content.Intent>,
            ) {
            }
        }
    TodoktodokTheme {
        MyScreen(
            discussionDetailNavigation = discussionNavigation,
            selectBookNavigation = selectBookNavigation,
            settingNavigation = settingNavigation,
            uiState = uiState,
            isLoading = true,
            onChangeShowMyDiscussion = {},
            navigateToDiscussion = {},
            onImageSelected = {},
            onRefresh = {},
            onCompleteShowDiscussionDetail = { },
        )
    }
}
