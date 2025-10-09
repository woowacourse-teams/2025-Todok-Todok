package com.team.todoktodok.presentation.compose.bookdiscussions.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.team.todoktodok.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDiscussionsTopAppBar(
    onAppIconClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {},
        modifier = modifier,
        navigationIcon = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier =
                    Modifier.clickable(onClick = onAppIconClick),
            ) {
                Icon(
                    painter = painterResource(R.drawable.img_mascort),
                    tint = null,
                    contentDescription = "토론방 목록 화면 이동 버튼 이미지",
                )

                Icon(
                    painter = painterResource(R.drawable.img_app_name),
                    tint = null,
                    contentDescription = "토론방 목록 화면 이동 버튼 텍스트",
                )
            }
        },
        actions = {
            Icon(
                painter = painterResource(R.drawable.ic_profile),
                tint = null,
                contentDescription = "프로필 화면 이동 버튼",
                modifier = Modifier.clickable(onClick = onProfileClick),
            )
        },
    )
}
