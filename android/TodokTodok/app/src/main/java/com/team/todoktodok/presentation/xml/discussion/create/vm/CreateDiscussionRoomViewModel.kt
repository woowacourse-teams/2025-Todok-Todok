package com.team.todoktodok.presentation.xml.discussion.create.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.team.domain.repository.BookRepository
import com.team.domain.repository.DiscussionRepository
import com.team.domain.repository.TokenRepository
import com.team.todoktodok.presentation.core.event.MutableSingleLiveData
import com.team.todoktodok.presentation.core.event.SingleLiveData
import com.team.todoktodok.presentation.xml.discussion.create.CreateDiscussionUiEvent
import com.team.todoktodok.presentation.xml.discussion.create.CreateDiscussionUiState
import com.team.todoktodok.presentation.xml.discussion.create.ErrorCreateDiscussionType
import com.team.todoktodok.presentation.xml.discussion.create.SerializationCreateDiscussionRoomMode

class CreateDiscussionRoomViewModel(
    private val mode: SerializationCreateDiscussionRoomMode,
    private val bookRepository: BookRepository,
    private val discussionRepository: DiscussionRepository,
    private val tokenRepository: TokenRepository,
) : ViewModel() {
    private val _uiState: MutableLiveData<CreateDiscussionUiState> =
        MutableLiveData(CreateDiscussionUiState())
    val uiState: LiveData<CreateDiscussionUiState> = _uiState

    private val _uiEvent: MutableSingleLiveData<CreateDiscussionUiEvent> = MutableSingleLiveData()
    val uiEvent: SingleLiveData<CreateDiscussionUiEvent> get() = _uiEvent

    init {
        initBook()
    }

    fun initBook() {
        if (mode is SerializationCreateDiscussionRoomMode.Create) {
            val selectedBook = mode.selectedBook.toDomain()
            _uiState.value = _uiState.value?.copy(book = selectedBook)
        }
    }

    fun save() {
        _uiState.value?.book ?: run {
            _uiEvent.setValue(CreateDiscussionUiEvent.ShowToast(ErrorCreateDiscussionType.BOOK_INFO_NOT_FOUND))
            return
        }
        _uiState.value?.title ?: run {
            _uiEvent.setValue(CreateDiscussionUiEvent.ShowToast(ErrorCreateDiscussionType.TITLE_NOT_FOUND))
            return
        }
        _uiState.value?.opinion ?: run {
            _uiEvent.setValue(CreateDiscussionUiEvent.ShowToast(ErrorCreateDiscussionType.CONTENT_NOT_FOUND))
            return
        }
//        viewModelScope.launch { discussionRepository.saveDiscussionRoom(book, title, opinion) }
        _uiEvent.setValue(CreateDiscussionUiEvent.Finish)
    }

    fun finish() {
        _uiEvent.setValue(CreateDiscussionUiEvent.Finish)
    }

    fun updateTitle(title: String) {
        _uiState.value = _uiState.value?.copy(title = title)
    }

    fun updateOpinion(opinion: String) {
        _uiState.value = _uiState.value?.copy(opinion = opinion)
    }

    companion object {
        private const val MAX_OPINION_LENGTH: Int = 2500
        private const val MAX_TITLE_LENGTH: Int = 50
    }
}
