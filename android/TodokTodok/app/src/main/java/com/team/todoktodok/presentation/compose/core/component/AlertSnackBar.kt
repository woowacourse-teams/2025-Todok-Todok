package com.team.todoktodok.presentation.compose.core.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.todoktodok.R
import com.team.todoktodok.presentation.compose.theme.Gray4D
import com.team.todoktodok.presentation.compose.theme.GreenDF
import com.team.todoktodok.presentation.compose.theme.Pretendard

@Composable
fun AlertSnackBar(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier,
    @DrawableRes iconRes: Int = R.drawable.ic_alert,
    backgroundColor: Color = GreenDF,
    contentColor: Color = Gray4D,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(10.dp),
                ).padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            colorFilter = ColorFilter.tint(contentColor),
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = snackbarData.visuals.message,
            color = contentColor,
            fontSize = 16.sp,
            fontFamily = Pretendard,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Preview
@Composable
fun AlertSnackBarPreview() {
    val snackbarData =
        object : SnackbarData {
            override val visuals: SnackbarVisuals
                get() =
                    object : SnackbarVisuals {
                        override val actionLabel: String? = null
                        override val duration: SnackbarDuration = SnackbarDuration.Short
                        override val message: String = "This is a test message"
                        override val withDismissAction: Boolean = false
                    }

            override fun dismiss() = Unit

            override fun performAction() = Unit
        }
    AlertSnackBar(snackbarData = snackbarData)
}
