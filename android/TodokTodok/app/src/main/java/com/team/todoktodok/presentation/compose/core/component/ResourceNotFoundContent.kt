package com.team.todoktodok.presentation.compose.core.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.todoktodok.R

@Composable
fun ResourceNotFoundView(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    actionTitle: String? = null,
    onActionClick: (() -> Unit)? = null,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Image(
            painter = painterResource(R.drawable.img_mascort_outline),
            contentDescription = null,
            Modifier.size(150.dp),
        )

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontSize = 20.sp,
        )

        subtitle?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 14.sp,
            )
        }

        actionTitle?.let {
            Text(
                text = it,
                fontSize = 14.sp,
                style = MaterialTheme.typography.labelSmall,
                textDecoration = TextDecoration.Underline,
                modifier =
                    Modifier.clickable(enabled = onActionClick != null) {
                        onActionClick?.invoke()
                    },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ResourceNotFoundViewPreview() {
    ResourceNotFoundView(
        title = "데이터 없음",
        subtitle = "표시할 데이터가 없습니다.",
        actionTitle = "새로 만들기",
        onActionClick = {},
    )
}
