package com.team.todoktodok.presentation.compose.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.team.todoktodok.R
import com.team.todoktodok.presentation.compose.theme.Gray9F
import com.team.todoktodok.presentation.compose.theme.Green1A
import com.team.todoktodok.presentation.compose.theme.WhiteF9
import com.team.todoktodok.presentation.xml.book.SelectBookActivity

@Composable
fun MainBottomNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        windowInsets = NavigationBarDefaults.windowInsets,
        modifier = modifier,
        containerColor = WhiteF9,
    ) {
        BottomNavigationItem(
            selected = currentRoute == MainDestination.Discussion::class.qualifiedName,
            onClick = {
                if (currentRoute != MainDestination.Discussion::class.qualifiedName) {
                    navController.navigate(MainDestination.Discussion) {
                        launchSingleTop = true
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        restoreState = true
                    }
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.MenuBook,
                    tint = if (currentRoute == MainDestination.Discussion::class.qualifiedName) Green1A else Gray9F,
                    contentDescription = stringResource(R.string.bottom_navigation_content_description_discussion),
                )
            },
            label = {
                Text(
                    text = stringResource(R.string.bottom_navigation_discussion),
                    color = if (currentRoute == MainDestination.Discussion::class.qualifiedName) Green1A else Gray9F,
                    style = MaterialTheme.typography.labelMedium,
                )
            },
        )

        // ✏️ 글쓰기 버튼
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
            Icon(
                painter = painterResource(R.drawable.stylus_24px),
                tint = Color.White,
                contentDescription = stringResource(R.string.bottom_navigation_content_description_create_discussion),
                modifier =
                    Modifier
                        .size(50.dp)
                        .padding(10.dp),
            )
        }

        BottomNavigationItem(
            selected = currentRoute == MainDestination.My::class.qualifiedName,
            onClick = {
                if (currentRoute != MainDestination.My::class.qualifiedName) {
                    navController.navigate(MainDestination.My) {
                        launchSingleTop = true
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        restoreState = true
                    }
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.PersonOutline,
                    tint = if (currentRoute == MainDestination.My::class.qualifiedName) Green1A else Gray9F,
                    contentDescription = stringResource(R.string.bottom_navigation_content_description_my),
                )
            },
            label = {
                Text(
                    text = stringResource(R.string.bottom_navigation_my),
                    color = if (currentRoute == MainDestination.My::class.qualifiedName) Green1A else Gray9F,
                    style = MaterialTheme.typography.labelMedium,
                )
            },
        )
    }
}

@Preview
@Composable
private fun MainBottomNavigationPreview() {
    MainBottomNavigation(navController = rememberNavController())
}
