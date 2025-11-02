package com.team.todoktodok.presentation.compose.my

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.team.domain.model.ImagePayload
import com.team.todoktodok.presentation.compose.LocalUiExceptionHandler
import com.team.todoktodok.presentation.compose.core.ObserveAsEvents
import com.team.todoktodok.presentation.compose.core.component.CloverProgressBar
import com.team.todoktodok.presentation.compose.my.component.EditableProfileImage
import com.team.todoktodok.presentation.compose.my.component.Information
import com.team.todoktodok.presentation.compose.my.component.MyToolbar
import com.team.todoktodok.presentation.compose.my.component.ProfileTab
import com.team.todoktodok.presentation.compose.my.vm.MyProfileViewModel
import com.team.todoktodok.presentation.compose.preview.MyProfileUiStatePreviewParameterProvider
import com.team.todoktodok.presentation.compose.theme.TodoktodokTheme
import com.team.todoktodok.presentation.compose.theme.White

@Composable
fun MyScreen(
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
    TodoktodokTheme {
        MyScreen(
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
