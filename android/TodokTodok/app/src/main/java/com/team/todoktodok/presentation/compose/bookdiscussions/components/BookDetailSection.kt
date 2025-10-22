package com.team.todoktodok.presentation.compose.bookdiscussions.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.team.todoktodok.R
import com.team.todoktodok.presentation.compose.bookdiscussions.model.BookDetailSectionUiState
import com.team.todoktodok.presentation.compose.theme.Black18
import com.team.todoktodok.presentation.compose.theme.Gray66

private const val COLLAPSED_LINES = 1
private const val EXPENDED_LINES = 10

@Composable
fun BookDetailSection(
    bookDetailSectionUiState: BookDetailSectionUiState,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(100.dp),
        ) {
            AsyncImage(
                model = bookDetailSectionUiState.bookImage,
                contentDescription = stringResource(R.string.book_image),
                modifier =
                    Modifier
                        .fillMaxHeight()
                        .width(80.dp),
            )
            Spacer(Modifier.width(20.dp))
            FlowColumn(Modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceAround) {
                Text(
                    bookDetailSectionUiState.bookTitle,
                    fontSize = 16.sp,
                    color = Black18,
                    maxLines = 2,
                )
                Text(
                    bookDetailSectionUiState.bookAuthor,
                    fontSize = 16.sp,
                    color = Gray66,
                    maxLines = 1,
                )
                Text(
                    bookDetailSectionUiState.bookPublisher,
                    fontSize = 16.sp,
                    color = Gray66,
                    maxLines = 1,
                )
            }
        }
        Spacer(Modifier.height(20.dp))
        ExpandableSection("도서 소개", bookDetailSectionUiState.bookSummary)
    }
}

@Composable
fun ExpandableSection(
    title: String,
    body: String,
    modifier: Modifier = Modifier,
    collapsedLines: Int = COLLAPSED_LINES,
    expandedInitial: Boolean = false,
    sectionId: String? = null,
) {
    var expanded by rememberSaveable(listOf(sectionId)) { mutableStateOf(expandedInitial) }
    val rotation by animateFloatAsState(if (expanded) 0f else -90f)
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .animateContentSize(),
    ) {
        Row(
            modifier =
                Modifier
                    .semantics {
                        role = Role.Button
                    }.toggleable(value = expanded, role = Role.Button) { expanded = it },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = title, fontSize = 16.sp, color = Black18)
            Icon(
                painter = painterResource(R.drawable.btn_toggle_bottom_arrow),
                contentDescription = null,
                modifier =
                    Modifier
                        .size(24.dp)
                        .rotate(rotation),
            )
        }

        Text(
            text = stringResource(R.string.book_info_message).format(body),
            maxLines = if (expanded) EXPENDED_LINES else collapsedLines,
            overflow = TextOverflow.Ellipsis,
            fontSize = 16.sp,
            color = Gray66,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookDetailSectionPreview() {
    val bookDetailSectionUiState =
        BookDetailSectionUiState(
            bookTitle = "오브젝트 - 코드로 이해하는 객체지향 설계",
            bookAuthor = "조영호",
            bookImage = "https://dummyimage.com/200x300/cccccc/000000.png&text=Clean+Code",
            bookPublisher = "위키북스",
            bookSummary =
                "역할, 책임, 협력에 기반해 객체지향 프로그램을 설계하고 구현하는 방법, 응집도와 결합도를 이용해 설계를 트레이드오프하는 방법, 설계를 유연하게 만드는 다양한 의존성 관리 기법, 타입 계층을 위한 상속과 코드 재사용을 위한 합성의 개념 등을 다룬다.",
        )
    BookDetailSection(bookDetailSectionUiState, modifier = Modifier.height(300.dp))
}
