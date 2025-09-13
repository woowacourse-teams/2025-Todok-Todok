package com.team.todoktodok.presentation.view.notification

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun navigateToBack() {
    Icon(
        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
        contentDescription = "뒤로가기",
    )
}

@Preview(name = "뒤로가기 버튼", showBackground = true)
@Composable
fun navigateToBackPreview() {
    navigateToBack()
}
