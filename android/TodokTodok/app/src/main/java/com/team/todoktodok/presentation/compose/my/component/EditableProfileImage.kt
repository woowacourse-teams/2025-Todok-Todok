package com.team.todoktodok.presentation.compose.my.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.team.todoktodok.R
import com.team.todoktodok.presentation.compose.theme.Gray75
import com.team.todoktodok.presentation.compose.theme.GrayE0

@Composable
fun EditableProfileImage(
    profileImageUrl: String,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    Box(modifier = modifier.padding(top = 10.dp)) {
        AsyncImage(
            model =
                ImageRequest
                    .Builder(context)
                    .data(profileImageUrl)
                    .crossfade(true)
                    .build(),
            contentDescription = stringResource(R.string.content_description_my_profile_image),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.img_mascort),
            error = painterResource(id = R.drawable.img_mascort),
            fallback = painterResource(id = R.drawable.img_mascort),
            modifier =
                Modifier
                    .clip(CircleShape)
                    .size(90.dp)
                    .border(width = 2.dp, color = GrayE0, shape = CircleShape),
        )

        Box(
            modifier =
                Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(color = GrayE0)
                    .align(Alignment.BottomEnd),
        ) {
            Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = stringResource(R.string.content_description_icon_modify_my_profile_image),
                modifier =
                    Modifier
                        .size(20.dp)
                        .align(Alignment.Center),
                tint = Gray75,
            )
        }
    }
}

@Preview
@Composable
private fun ProfileImageComponentPreview() {
    EditableProfileImage(profileImageUrl = "")
}
