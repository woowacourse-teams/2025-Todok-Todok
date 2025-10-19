package com.team.todoktodok.presentation.compose.bookdiscussions.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.team.todoktodok.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDiscussionsTopAppBar(
    onNavigationIcon: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CenterAlignedTopAppBar(
        title = { Title() },
        modifier = modifier,
        navigationIcon = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier =
                    Modifier.clickable(onClick = onNavigationIcon),
            ) {
                Icon(
                    painter = painterResource(R.drawable.btn_back),
                    tint = null,
                    contentDescription = stringResource(R.string.bookdiscussions_content_description_navigate_to_discussions),
                )
            }
        },
    )
}

@Composable
private fun Title(modifier: Modifier = Modifier) {
    Text(
        stringResource(R.string.book_discussions_title),
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier,
    )
}

@Preview
@Composable
private fun BookDiscussionsTopAppBarPreview() {
    BookDiscussionsTopAppBar({})
}
