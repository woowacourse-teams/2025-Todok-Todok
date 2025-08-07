package com.team.todoktodok.presentation.view.discussion.create.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.Book
import com.team.domain.model.Discussion
import com.team.domain.repository.BookRepository
import com.team.domain.repository.DiscussionRepository
import com.team.todoktodok.data.datasource.token.TokenDataSource
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CreateDiscussionRoomViewModel(
    private val bookRepository: BookRepository,
    private val discussionRepository: DiscussionRepository,
    private val tokenDataSource: TokenDataSource,
) : ViewModel() {
    private val _book: MutableLiveData<Book> = MutableLiveData()
    val book: LiveData<Book> = _book

    private val _title: MutableLiveData<String> = MutableLiveData()
    val title: LiveData<String> = _title

    private val _opinion: MutableLiveData<String> = MutableLiveData()
    val opinion: LiveData<String> = _opinion

    private val _discussionRoomId: MutableLiveData<Long> = MutableLiveData()
    val discussionRoomId: LiveData<Long> = _discussionRoomId

    init {
        _book.value = Book(
            id = 1L,
            title = "클린 코드",
            author = "로버트 C. 마틴",
            image = "https://image.aladin.co.kr/product/1950/55/cover500/8960773417_2.jpg",
        )
    }

    fun onTitleChanged(title: String) {
        _title.value = title
    }

    fun onOpinionChanged(opinion: String) {
        _opinion.value = opinion
    }

    fun createDiscussionRoom() {
        val book = book.value ?: throw IllegalStateException("책 정보가 없습니다.")
        val title = title.value ?: throw IllegalStateException("제목이 없습니다.")
        val opinion = opinion.value ?: throw IllegalStateException("내용이 없습니다.")
        viewModelScope.launch {
            val bookId = bookRepository.saveBook(book)
            val discussionId =
                discussionRepository.saveDiscussionRoom(
                    bookId = bookId,
                    discussionTitle = title,
                    discussionOpinion = opinion
                )
            _discussionRoomId.value = discussionId
            return@launch
        }
    }

    fun getDiscussionRoom() {
        val discussionRoomId = discussionRoomId.value ?: throw IllegalStateException("토론방 정보가 없습니다.")
        viewModelScope.launch {
            val result: Result<Discussion> = discussionRepository.getDiscussion(discussionRoomId)
        }
    }
}