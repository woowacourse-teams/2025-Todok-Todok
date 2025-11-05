package com.team.ui_compose.my.component

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.core.navigation.SettingRoute
import com.team.ui_compose.R
import com.team.ui_compose.common.extension.noRippleClickable
import com.team.core.R as coreR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyToolbar(
    settingNavigation: SettingRoute,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val activityLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                onRefresh()
            }
        }

    Column(
        modifier =
            modifier
                .background(color = White),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(White)
                    .padding(top = 12.dp)
                    .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(coreR.drawable.img_mascort),
                    tint = null,
                    contentDescription = null,
                )
                Icon(
                    painter = painterResource(R.drawable.img_app_name),
                    tint = null,
                    contentDescription = null,
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = stringResource(R.string.content_description_discussions_toolbar_setting),
                    modifier =
                        Modifier.noRippleClickable {
                            settingNavigation.navigateToSettingForResult(
                                fromActivity = context as androidx.activity.ComponentActivity,
                                resultLauncher = activityLauncher,
                            )
                        },
                )

                Spacer(modifier = Modifier.width(10.dp))
            }
        }
    }
}

@Preview
@Composable
private fun MyToolbarPreview() {
    val settingNavigation =
        object : SettingRoute {
            override fun navigateToSetting(fromActivity: Activity) {
            }

            override fun navigateToSettingForResult(
                fromActivity: Activity,
                resultLauncher: androidx.activity.result.ActivityResultLauncher<android.content.Intent>,
            ) {
            }
        }
    MyToolbar(
        settingNavigation = settingNavigation,
        onRefresh = {},
    )
}
