package com.team.todoktodok.presentation.compose.my.component

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
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
import com.team.domain.model.ImagePayload
import com.team.todoktodok.R
import com.team.todoktodok.presentation.compose.LocalUiExceptionHandler
import com.team.todoktodok.presentation.compose.theme.Gray75
import com.team.todoktodok.presentation.compose.theme.GrayE0
import com.team.todoktodok.presentation.core.ImagePayloadMapper

@Composable
fun EditableProfileImage(
    profileImageUrl: String,
    onImageSelected: (ImagePayload) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val exceptionHandler = LocalUiExceptionHandler.current
    val contentResolver = context.contentResolver
    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                runCatching { ImagePayloadMapper(contentResolver).from(uri) }
                    .onSuccess { image -> onImageSelected(image) }
                    .onFailure { exception ->
                        val message = context.getString(R.string.profile_can_not_load_image)
                        exceptionHandler.showErrorMessage(coroutineScope, message)
                    }
            }
        }

    Box(modifier = modifier.padding(top = 10.dp)) {
        AsyncImage(
            model =
                ImageRequest
                    .Builder(context)
                    .data(profileImageUrl)
                    .crossfade(true)
                    .build(),
            contentDescription = stringResource(R.string.content_description_my_profile_image),
            error = painterResource(id = R.drawable.img_mascort),
            fallback = painterResource(id = R.drawable.img_mascort),
            contentScale = ContentScale.Crop,
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
                    .align(Alignment.BottomEnd)
                    .clickable {
                        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    },
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
    EditableProfileImage(
        profileImageUrl = "",
        onImageSelected = {},
    )
}
