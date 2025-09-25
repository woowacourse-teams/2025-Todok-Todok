package com.team.todoktodok.presentation.xml.discussion.create.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.Book
import com.team.domain.model.discussionroom.DiscussionRoom
import com.team.domain.model.discussionroom.DiscussionRoom.Companion.DiscussionRoom
import com.team.domain.model.exception.onFailure
import com.team.domain.model.exception.onSuccess
import com.team.domain.model.exception.onSuccessSuspend
import com.team.domain.repository.BookRepository
import com.team.domain.repository.DiscussionRepository
import com.team.domain.repository.TokenRepository
import com.team.todoktodok.presentation.core.event.MutableSingleLiveData
import com.team.todoktodok.presentation.core.event.SingleLiveData
import com.team.todoktodok.presentation.xml.discussion.create.CreateDiscussionUiEvent
import com.team.todoktodok.presentation.xml.discussion.create.CreateDiscussionUiState
import com.team.todoktodok.presentation.xml.discussion.create.ErrorCreateDiscussionType
import com.team.todoktodok.presentation.xml.discussion.create.SerializationCreateDiscussionRoomMode
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class CreateDiscussionRoomViewModel(
    private val mode: SerializationCreateDiscussionRoomMode,
    private val bookRepository: BookRepository,
    private val discussionRepository: DiscussionRepository,
    private val tokenRepository: TokenRepository,
) : ViewModel() {
    private val _uiState: MutableLiveData<CreateDiscussionUiState> =
        MutableLiveData(
            CreateDiscussionUiState(),
        )
    val uiState: LiveData<CreateDiscussionUiState> = _uiState

    private val _uiEvent: MutableSingleLiveData<CreateDiscussionUiEvent> = MutableSingleLiveData()
    val uiEvent: SingleLiveData<CreateDiscussionUiEvent> get() = _uiEvent

    init {
        decideMode()
    }

    fun checkIsPossibleToSave() {
        viewModelScope.launch {
            val discussion = async { discussionRepository.getDiscussion() }.await()
            if (discussion == null) {
                _uiEvent.setValue(CreateDiscussionUiEvent.SaveDraft(true))
                return@launch
            }
            _uiEvent.setValue(CreateDiscussionUiEvent.SaveDraft(false))
        }
    }

    private fun decideMode() {
        when (mode) {
            is SerializationCreateDiscussionRoomMode.Create -> {
                _uiState.value = _uiState.value?.copy(book = mode.selectedBook.toDomain())
            }

            is SerializationCreateDiscussionRoomMode.Edit -> {
                _uiState.value = _uiState.value?.copy(discussionRoomId = mode.discussionRoomId)
                getDiscussionRoom(mode.discussionRoomId)
            }

            is SerializationCreateDiscussionRoomMode.Draft -> {
                viewModelScope.launch {
                    val (book, discussion) =
                        awaitAll(
                            async { discussionRepository.getBook() },
                            async { discussionRepository.getDiscussion() },
                        )

                    val bookResult = book as Book
                    val discussionResult = discussion as DiscussionRoom
                    _uiState.value =
                        _uiState.value?.copy(
                            book = bookResult,
                            title = discussionResult.title,
                            opinion = discussionResult.opinion,
                        )
                }
            }
        }
    }

    fun saveDraft() {
        val book =
            _uiState.value?.book ?: run {
                _uiEvent.setValue(CreateDiscussionUiEvent.ShowToast(ErrorCreateDiscussionType.BOOK_INFO_NOT_FOUND))
                return
            }
        val title =
            _uiState.value?.title ?: run {
                _uiEvent.setValue(CreateDiscussionUiEvent.ShowToast(ErrorCreateDiscussionType.TITLE_NOT_FOUND))
                return
            }
        val opinion =
            _uiState.value?.opinion ?: run {
                _uiEvent.setValue(CreateDiscussionUiEvent.ShowToast(ErrorCreateDiscussionType.CONTENT_NOT_FOUND))
                return
            }
        viewModelScope.launch { discussionRepository.saveDiscussionRoom(book, title, opinion) }
        _uiEvent.setValue(CreateDiscussionUiEvent.Finish)
    }

    fun finish() {
        _uiEvent.setValue(CreateDiscussionUiEvent.Finish)
    }

    fun updateTitle(title: String) {
        _uiState.value = _uiState.value?.copy(title = title)
        if (title.length == MAX_TITLE_LENGTH) {
            _uiEvent.setValue(CreateDiscussionUiEvent.ShowToast(ErrorCreateDiscussionType.TITLE_LENGTH_OVER))
        }
    }

    fun updateOpinion(opinion: String) {
        _uiState.value = _uiState.value?.copy(opinion = opinion)
        if (opinion.length == MAX_OPINION_LENGTH) {
            _uiEvent.setValue(CreateDiscussionUiEvent.ShowToast(ErrorCreateDiscussionType.OPINION_LENGTH_OVER))
        }
    }

    fun createDiscussionRoom() {
        val book =
            _uiState.value?.book
                ?: run {
                    updateErrorCreateDiscussion(ErrorCreateDiscussionType.BOOK_INFO_NOT_FOUND)
                    return
                }
        val title =
            _uiState.value?.title
                ?: run {
                    updateErrorCreateDiscussion(ErrorCreateDiscussionType.TITLE_NOT_FOUND)
                    return
                }
        val opinion =
            _uiState.value?.opinion
                ?: run {
                    updateErrorCreateDiscussion(ErrorCreateDiscussionType.CONTENT_NOT_FOUND)
                    return
                }
        viewModelScope.launch {
            if (mode is SerializationCreateDiscussionRoomMode.Draft) {
                discussionRepository.deleteDiscussionRoom()
            }
            bookRepository
                .saveBook(book)
                .onSuccessSuspend { bookId ->
                    val discussionId =
                        async {
                            discussionRepository.saveDiscussionRoom(
                                bookId = bookId,
                                discussionTitle = title,
                                discussionOpinion = opinion,
                            )
                        }
                    _uiEvent.setValue(
                        CreateDiscussionUiEvent.NavigateToDiscussionDetail(
                            discussionId.await(),
                            mode,
                        ),
                    )
                }.onFailure { exception ->
                    _uiEvent.setValue(CreateDiscussionUiEvent.ShowNetworkErrorMessage(exception))
                }
        }
    }

    private fun getDiscussionRoom(discussionRoomId: Long) {
        viewModelScope.launch {
            val myMemberId: Long = tokenRepository.getMemberId()
            discussionRepository.getDiscussion(discussionRoomId).onSuccess { result ->
                if (result.writer.id != myMemberId) {
                    updateErrorCreateDiscussion(ErrorCreateDiscussionType.NOT_MY_DISCUSSION_ROOM)
                }
                _uiState.value =
                    _uiState.value?.copy(
                        book = result.book,
                        title = result.discussionTitle,
                        opinion = result.discussionOpinion,
                    )
            }
        }
    }

    fun editDiscussionRoom() {
        val discussionRoomId =
            _uiState.value?.discussionRoomId ?: run {
                updateErrorCreateDiscussion(
                    ErrorCreateDiscussionType.DISCUSSION_ROOM_INFO_NOT_FOUND,
                )
                return
            }
        val title =
            _uiState.value?.title
                ?: run {
                    updateErrorCreateDiscussion(ErrorCreateDiscussionType.TITLE_NOT_FOUND)
                    return
                }
        val opinion =
            _uiState.value?.opinion ?: run {
                updateErrorCreateDiscussion(
                    ErrorCreateDiscussionType.CONTENT_NOT_FOUND,
                )
                return
            }

        val discussionRoom = DiscussionRoom(title, opinion)
        viewModelScope.launch {
            discussionRepository
                .editDiscussionRoom(
                    discussionId = discussionRoomId,
                    discussionRoom = discussionRoom,
                ).onSuccess {
                    _uiEvent.setValue(
                        CreateDiscussionUiEvent.NavigateToDiscussionDetail(
                            discussionRoomId,
                            mode,
                        ),
                    )
                }.onFailure { exception ->
                    _uiEvent.setValue(CreateDiscussionUiEvent.ShowNetworkErrorMessage(exception))
                }
        }
    }

    private fun updateErrorCreateDiscussion(errorCreateDiscussionType: ErrorCreateDiscussionType) {
        _uiEvent.setValue(CreateDiscussionUiEvent.ShowToast(errorCreateDiscussionType))
    }

    companion object {
        private const val MAX_OPINION_LENGTH: Int = 2500
        private const val MAX_TITLE_LENGTH: Int = 50
    }
}
