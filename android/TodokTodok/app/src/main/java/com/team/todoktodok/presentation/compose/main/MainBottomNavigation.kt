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
import androidx.compose.ui.platform.LocalContext
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
import com.team.todoktodok.presentation.xml.book.SelectBookActivity

@Composable
fun MainBottomNavigation(
    navController: NavHostController,
    selectedDestination: MainDestination,
    onSelectedDestinationChanged: (MainDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    NavigationBar(
        windowInsets = NavigationBarDefaults.windowInsets,
        modifier = modifier,
        containerColor = WhiteF9,
    ) {
        BottomNavigationItem(
            selected = selectedDestination.ordinal == MainDestination.Discussion.ordinal,
            onClick = {
                if (selectedDestination.ordinal != MainDestination.Discussion.ordinal) {
                    navController.navigate(MainDestination.Discussion.route) {
                        launchSingleTop = true
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        restoreState = true
                    }
                    onSelectedDestinationChanged(MainDestination.Discussion)
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.MenuBook,
                    tint = if (selectedDestination.ordinal == MainDestination.Discussion.ordinal) Green1A else Gray9F,
                    contentDescription = stringResource(MainDestination.Discussion.contentDescription),
                )
            },
            label = {
                Text(
                    text = stringResource(MainDestination.Discussion.label),
                    color = if (selectedDestination.ordinal == MainDestination.Discussion.ordinal) Green1A else Gray9F,
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
                    .clickable {
                        context.startActivity(SelectBookActivity.Intent(context))
                    },
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_create_discussion),
                contentDescription = stringResource(R.string.bottom_navigation_content_description_create_discussion),
                modifier = Modifier.size(50.dp),
            )
        }

        BottomNavigationItem(
            selected = selectedDestination.ordinal == MainDestination.My.ordinal,
            onClick = {
                if (selectedDestination.ordinal != MainDestination.My.ordinal) { 
                    navController.navigate(MainDestination.My.route) {
                        launchSingleTop = true
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        restoreState = true
                    }
                    onSelectedDestinationChanged(MainDestination.My)
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.PersonOutline,
                    tint = if (selectedDestination.ordinal == MainDestination.My.ordinal) Green1A else Gray9F,
                    contentDescription = stringResource(MainDestination.My.contentDescription),
                )
            },
            label = {
                Text(
                    text = stringResource(MainDestination.My.label),
                    color = if (selectedDestination.ordinal == MainDestination.My.ordinal) Green1A else Gray9F,
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
        selectedDestination = MainDestination.Discussion,
        onSelectedDestinationChanged = {},
    )
}
