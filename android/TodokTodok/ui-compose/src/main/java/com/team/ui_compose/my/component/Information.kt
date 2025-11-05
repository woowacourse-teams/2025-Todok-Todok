package com.team.ui_compose.my.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.core.R as coreR

@Composable
fun Information(
    nickname: String,
    profileMessage: String?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .padding(start = 15.dp),
    ) {
        Text(
            text = nickname,
            style = MaterialTheme.typography.titleMedium,
            modifier =
                Modifier
                    .align(Alignment.Start),
        )

        Text(
            text =
                profileMessage?.ifEmpty { stringResource(coreR.string.all_message_placeholder) }
                    ?: stringResource(coreR.string.all_message_placeholder),
            style = MaterialTheme.typography.labelLarge,
            modifier =
                Modifier
                    .align(Alignment.Start)
                    .padding(top = 5.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun InformationPreview() {
    Information(
        nickname = "페토",
        profileMessage = "하나코 나나 많관부",
    )
}
