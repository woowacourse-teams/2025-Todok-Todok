package com.team.todoktodok.presentation.compose.my

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.team.todoktodok.App
import com.team.todoktodok.presentation.compose.main.MainDestination
import com.team.todoktodok.presentation.compose.my.component.EditableProfileImage
import com.team.todoktodok.presentation.compose.my.component.Information
import com.team.todoktodok.presentation.compose.my.component.MyToolbar
import com.team.todoktodok.presentation.compose.my.component.ProfileTab
import com.team.todoktodok.presentation.compose.my.model.MyProfileUiState
import com.team.todoktodok.presentation.compose.my.vm.MyProfileViewModel
import com.team.todoktodok.presentation.compose.my.vm.MyProfileViewModelFactory
import com.team.todoktodok.presentation.compose.preview.MyProfileUiStatePreviewParameterProvider
import com.team.todoktodok.presentation.compose.theme.TodoktodokTheme
import com.team.todoktodok.presentation.compose.theme.White

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
        viewModel.loadProfile()
    }

    MyScreen(
        navController = navController,
        onChangeBottomNavigationTab = onChangeBottomNavigationTab,
        uiState = uiState.value,
        modifier = modifier,
    )
}

@Composable
fun MyScreen(
    navController: NavHostController,
    uiState: MyProfileUiState,
    onChangeBottomNavigationTab: (MainDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(color = White),
    ) {
        MyToolbar()

        EditableProfileImage(
            profileImageUrl = uiState.profile.profileImage,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )

        Information(
            nickname = uiState.profile.nickname,
            profileMessage = uiState.profile.message,
        )

        ProfileTab(
            navController = navController,
            onChangeBottomNavigationTab = onChangeBottomNavigationTab,
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(top = 10.dp),
        )
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
            navController = NavHostController(LocalContext.current),
            onChangeBottomNavigationTab = {},
            uiState = uiState,
        )
    }
}
