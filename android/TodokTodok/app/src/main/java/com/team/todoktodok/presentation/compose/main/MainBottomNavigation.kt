package com.team.todoktodok.presentation.compose.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.team.todoktodok.R
import com.team.todoktodok.presentation.compose.theme.Gray9F
import com.team.todoktodok.presentation.compose.theme.Green1A
import com.team.todoktodok.presentation.compose.theme.WhiteF9

@Composable
fun MainBottomNavigation(
    navController: NavHostController,
    selectedDestination: Int,
    onSelectedDestinationChanged: (Int) -> Unit,
    onClickCreateDiscussion: () -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        windowInsets = NavigationBarDefaults.windowInsets,
        modifier = modifier,
        containerColor = WhiteF9,
    ) {
        BottomNavigationItem(
            selected = selectedDestination == 0,
            onClick = {
                navController.navigate(MainDestination.Discussion.route) {
                    launchSingleTop = true
                }
                onSelectedDestinationChanged(MainDestination.Discussion.ordinal)
            },
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.MenuBook,
                    tint = if (selectedDestination == MainDestination.Discussion.ordinal) Green1A else Gray9F,
                    contentDescription = stringResource(MainDestination.Discussion.contentDescription),
                )
            },
            label = {
                Text(
                    text = stringResource(MainDestination.Discussion.label),
                    color = if (selectedDestination == MainDestination.Discussion.ordinal) Green1A else Gray9F,
                    style = MaterialTheme.typography.labelMedium,
                )
            },
        )

        Box(
            modifier =
                Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Green1A)
                    .clickable { onClickCreateDiscussion() },
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_create_discussion),
                contentDescription = stringResource(R.string.bottom_navigation_content_description_create_discussion),
                modifier = Modifier.size(50.dp),
            )
        }

        BottomNavigationItem(
            selected = selectedDestination == MainDestination.My.ordinal,
            onClick = {
                navController.navigate(MainDestination.My.route) {
                    launchSingleTop = true
                }
                onSelectedDestinationChanged(MainDestination.My.ordinal)
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.PersonOutline,
                    tint = if (selectedDestination == MainDestination.My.ordinal) Green1A else Gray9F,
                    contentDescription = stringResource(MainDestination.My.contentDescription),
                )
            },
            label = {
                Text(
                    text = stringResource(MainDestination.My.label),
                    color = if (selectedDestination == MainDestination.My.ordinal) Green1A else Gray9F,
                    style = MaterialTheme.typography.labelMedium,
                )
            },
        )
    }
}

@Preview
@Composable
private fun MainBottomNavigationPreview() {
    MainBottomNavigation(
        navController = rememberNavController(),
        selectedDestination = 0,
        onSelectedDestinationChanged = {},
        onClickCreateDiscussion = {},
    )
}
