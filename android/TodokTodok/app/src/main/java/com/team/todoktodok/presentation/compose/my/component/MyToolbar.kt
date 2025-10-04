package com.team.todoktodok.presentation.compose.my.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.todoktodok.R
import com.team.todoktodok.presentation.compose.core.extension.noRippleClickable
import com.team.todoktodok.presentation.compose.theme.White
import com.team.todoktodok.presentation.xml.setting.SettingActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyToolbar(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Column(
        modifier =
            modifier
                .background(color = White),
    ) {
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier =
                        Modifier
                            .fillMaxSize(),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.img_mascort),
                        tint = null,
                        contentDescription = null,
                    )

                    Icon(
                        painter = painterResource(R.drawable.img_app_name),
                        tint = null,
                        contentDescription = null,
                    )
                }
            },
            actions = {
                Row {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = stringResource(R.string.content_description_discussions_toolbar_setting),
                        modifier =
                            Modifier.noRippleClickable(onClick = {
                                context.startActivity(SettingActivity.Intent(context))
                            }),
                    )

                    Spacer(modifier = Modifier.width(10.dp))
                }
            },
            colors =
                TopAppBarDefaults.topAppBarColors(
                    containerColor = White,
                ),
        )
    }
}

@Preview
@Composable
private fun MyToolbarPreview() {
    MyToolbar()
}
