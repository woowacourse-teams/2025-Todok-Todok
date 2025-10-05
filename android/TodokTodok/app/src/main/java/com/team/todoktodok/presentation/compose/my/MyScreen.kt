package com.team.todoktodok.presentation.compose.my

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.team.domain.model.ImagePayload
import com.team.todoktodok.App
import com.team.todoktodok.presentation.compose.main.MainDestination
import com.team.todoktodok.presentation.compose.my.component.EditableProfileImage
import com.team.todoktodok.presentation.compose.my.component.Information
import com.team.todoktodok.presentation.compose.my.component.MyToolbar
import com.team.todoktodok.presentation.compose.my.component.ProfileTab
import com.team.todoktodok.presentation.compose.my.vm.MyProfileViewModel
import com.team.todoktodok.presentation.compose.my.vm.MyProfileViewModelFactory
import com.team.todoktodok.presentation.compose.preview.MyProfileUiStatePreviewParameterProvider
import com.team.todoktodok.presentation.compose.theme.TodoktodokTheme
import com.team.todoktodok.presentation.compose.theme.White
import com.team.todoktodok.presentation.xml.serialization.SerializationDiscussion

@Composable
fun MyScreen(
    navController: NavHostController,
    onChangeBottomNavigationTab: (MainDestination) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MyProfileViewModel =
        viewModel(
            factory =
                MyProfileViewModelFactory(
                    (LocalContext.current.applicationContext as App).container,
                ),
        ),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadInitialProfile()
    }

    MyScreen(
        uiState = uiState.value,
        onChangeBottomNavigationTab = onChangeBottomNavigationTab,
        onChangeShowMyDiscussion = viewModel::toggleShowMyDiscussion,
        onCompleteRemoveDiscussion = viewModel::removeDiscussion,
        onCompleteModifyDiscussion = viewModel::modifyDiscussion,
        onImageSelected = viewModel::modifyProfileImage,
        modifier = modifier,
        navController = navController,
    )
}

@Composable
fun MyScreen(
    uiState: MyProfileUiState,
    navController: NavHostController,
    onChangeBottomNavigationTab: (MainDestination) -> Unit,
    onCompleteModifyDiscussion: (SerializationDiscussion) -> Unit,
    onCompleteRemoveDiscussion: (Long) -> Unit,
    onChangeShowMyDiscussion: (Boolean) -> Unit,
    onImageSelected: (ImagePayload) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier =
            modifier
                .fillMaxSize()
                .background(color = White),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            MyToolbar()
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
                navController = navController,
                onChangeBottomNavigationTab = onChangeBottomNavigationTab,
                onChangeShowMyDiscussion = { onChangeShowMyDiscussion(it) },
                onCompleteRemoveDiscussion = onCompleteRemoveDiscussion,
                onCompleteModifyDiscussion = onCompleteModifyDiscussion,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
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
            onChangeBottomNavigationTab = {},
            onChangeShowMyDiscussion = {},
            onCompleteRemoveDiscussion = {},
            onCompleteModifyDiscussion = {},
            onImageSelected = {},
            navController = NavHostController(LocalContext.current),
        )
    }
}
