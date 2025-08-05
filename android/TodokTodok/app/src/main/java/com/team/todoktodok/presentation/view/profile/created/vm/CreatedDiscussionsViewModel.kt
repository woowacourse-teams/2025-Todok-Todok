package com.team.todoktodok.presentation.view.profile.created.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.Book
import com.team.domain.model.Discussion
import com.team.domain.model.member.Nickname
import com.team.domain.model.member.User
import com.team.domain.repository.MemberRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class CreatedDiscussionsViewModel(
    private val memberRepository: MemberRepository,
) : ViewModel() {
    private val _discussion = MutableLiveData(emptyList<Discussion>())
    val discussion: LiveData<List<Discussion>> get() = _discussion

    fun loadDiscussions(id: String?) {
        viewModelScope.launch {
            // val result = memberRepository.getMemberDiscussionRooms(MemberId(id), MemberDiscussionType.CREATED)
            _discussion.value =
                listOf(
                    Discussion(
                        id = 1L,
                        discussionTitle = "JPA 성능 최적화",
                        book = Book(1L, "자바 ORM 표준 JPA 프로그래밍", "김영한", ""),
                        writer = User(1L, Nickname("홍길동")),
                        createAt = LocalDateTime.of(2025, 7, 12, 12, 0),
                        discussionOpinion = "fetch join을 남발하면 안됩니다.",
                    ),
                    Discussion(
                        id = 2L,
                        discussionTitle = "코틀린 코루틴 완전 정복",
                        book = Book(2L, "Kotlin in Action", "Dmitry Jemerov", ""),
                        writer = User(2L, Nickname("박코루틴")),
                        createAt = LocalDateTime.of(2025, 7, 13, 12, 0),
                        discussionOpinion = "suspend fun과 launch 차이를 이해해야 합니다.",
                    ),
                )
        }
    }
}
