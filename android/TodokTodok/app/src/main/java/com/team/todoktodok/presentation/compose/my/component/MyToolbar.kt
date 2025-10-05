package com.team.todoktodok.presentation.compose.my.component

import android.app.Activity
import android.util.Log
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
fun MyToolbar(
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val activityLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                Log.d("dasda", "MyToolbar: ")
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

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = stringResource(R.string.content_description_discussions_toolbar_setting),
                    modifier =
                        Modifier.noRippleClickable {
                            activityLauncher.launch(
                                SettingActivity.Intent(context),
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
    MyToolbar(
        onRefresh = {},
    )
}
