package com.team.todoktodok.presentation.view.discussion.create.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.Book
import com.team.domain.model.Discussion
import com.team.domain.model.member.DiscussionRoom.Companion.DiscussionRoom
import com.team.domain.repository.BookRepository
import com.team.domain.repository.DiscussionRepository
import com.team.domain.repository.TokenRepository
import com.team.todoktodok.presentation.core.event.MutableSingleLiveData
import com.team.todoktodok.presentation.core.event.SingleLiveData
import com.team.todoktodok.presentation.view.discussion.create.CreateDiscussionUiEvent
import com.team.todoktodok.presentation.view.discussion.create.SerializationCreateDiscussionRoomMode
import kotlinx.coroutines.launch

class CreateDiscussionRoomViewModel(
    private val mode: SerializationCreateDiscussionRoomMode,
    private val bookRepository: BookRepository,
    private val discussionRepository: DiscussionRepository,
    private val tokenRepository: TokenRepository,
) : ViewModel() {
    private val _isCreate: MutableLiveData<Boolean> = MutableLiveData(false)
    val isCreate: LiveData<Boolean> = _isCreate

    private val _book: MutableLiveData<Book> = MutableLiveData()
    val book: LiveData<Book> = _book

    private val _title: MutableLiveData<String> = MutableLiveData()
    val title: LiveData<String> = _title

    private val _opinion: MutableLiveData<String> = MutableLiveData()
    val opinion: LiveData<String> = _opinion

    private var discussionRoomId: Long? = null

    private val _uiEvent: MutableSingleLiveData<CreateDiscussionUiEvent> = MutableSingleLiveData()
    val uiEvent: SingleLiveData<CreateDiscussionUiEvent> get() = _uiEvent

    init {
        decideMode()
    }

    private fun decideMode() {
        when (mode) {
            is SerializationCreateDiscussionRoomMode.Create -> {
                _book.value = mode.selectedBook.toDomain()
            }

            is SerializationCreateDiscussionRoomMode.Edit -> {
                discussionRoomId = mode.discussionRoomId
                getDiscussionRoom(mode.discussionRoomId)
            }
        }
    }

    fun onTitleChanged(title: String) {
        _title.value = title
        _isCreate.value = title.isNotEmpty() && opinion.value?.isNotEmpty() == true
    }

    fun onOpinionChanged(opinion: String) {
        _opinion.value = opinion
        _isCreate.value = title.value?.isNotEmpty() == true && opinion.isNotEmpty()
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
                    discussionOpinion = opinion,
                )
            _uiEvent.setValue(CreateDiscussionUiEvent.NavigateToDiscussionDetail(discussionId))
        }
    }

    private fun getDiscussionRoom(discussionRoomId: Long) {
        viewModelScope.launch {
            val myMemberId: Long = tokenRepository.getMemberId()
            val result: Discussion =
                discussionRepository.getDiscussion(discussionRoomId).getOrThrow()

            if (result.writer.id != myMemberId) {
                throw IllegalStateException("내가 작성한 토론방이 아닙니다")
            }
            _book.value = result.book
            _title.value = result.discussionTitle
            _opinion.value = result.discussionOpinion
        }
    }

    fun editDiscussionRoom() {
        try {
            val discussionRoomId =
                discussionRoomId ?: throw IllegalStateException("토론방 정보가 없습니다.")
            val title = title.value ?: throw IllegalStateException("제목이 없습니다.")
            val opinion = opinion.value ?: throw IllegalStateException("내용이 없습니다.")
            val discussionRoom = DiscussionRoom(title, opinion)
            viewModelScope.launch {
                discussionRepository.editDiscussionRoom(
                    discussionId = discussionRoomId,
                    discussionRoom = discussionRoom,
                )
                _uiEvent.setValue(
                    CreateDiscussionUiEvent.NavigateToDiscussionDetail(
                        discussionRoomId
                    )
                )
            }
        } catch (e: IllegalArgumentException) {
            _uiEvent.setValue(CreateDiscussionUiEvent.ShowToast(e.message ?: "다시 시도해주세요"))
        } catch (e: IllegalStateException) {
            _uiEvent.setValue(CreateDiscussionUiEvent.ShowToast(e.message ?: "다시 시도해주세요"))
        }
    }
}
