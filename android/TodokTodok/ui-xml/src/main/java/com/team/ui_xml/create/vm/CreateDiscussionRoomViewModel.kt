package com.team.ui_xml.create.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.core.event.MutableSingleLiveData
import com.team.core.event.SingleLiveData
import com.team.domain.model.book.SearchedBook
import com.team.domain.model.discussionroom.DiscussionRoom
import com.team.domain.model.exception.onFailure
import com.team.domain.model.exception.onSuccess
import com.team.domain.model.exception.onSuccessSuspend
import com.team.domain.repository.BookRepository
import com.team.domain.repository.DiscussionRepository
import com.team.domain.repository.TokenRepository
import com.team.ui_xml.create.CreateDiscussionUiEvent
import com.team.ui_xml.create.CreateDiscussionUiState
import com.team.ui_xml.create.ErrorCreateDiscussionType
import com.team.ui_xml.create.SerializationCreateDiscussionRoomMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateDiscussionRoomViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val bookRepository: BookRepository,
        private val discussionRepository: DiscussionRepository,
        private val tokenRepository: TokenRepository,
    ) : ViewModel() {
        val mode: SerializationCreateDiscussionRoomMode =
            savedStateHandle.get<SerializationCreateDiscussionRoomMode>(EXTRA_MODE)
                ?: throw IllegalStateException(MODE_NOT_EXIST)

        private val _uiState: MutableLiveData<CreateDiscussionUiState> =
            MutableLiveData(
                CreateDiscussionUiState(),
            )
        val uiState: LiveData<CreateDiscussionUiState> = _uiState

        private val _uiEvent: MutableSingleLiveData<CreateDiscussionUiEvent> = MutableSingleLiveData()
        val uiEvent: SingleLiveData<CreateDiscussionUiEvent> get() = _uiEvent

        init {
            decideMode()
            getDraftDiscussionCount()
        }

        fun getDraft(id: Long) {
            viewModelScope.launch {
                val (book, discussion) =
                    awaitAll(
                        async { discussionRepository.getBook(id) },
                        async { discussionRepository.getDraftDiscussion(id) },
                    )
                val bookResult = book as SearchedBook
                val discussionResult = discussion as DiscussionRoom
                _uiState.value =
                    _uiState.value?.copy(
                        book = null,
                        draftBook = bookResult,
                        title = discussionResult.title,
                        opinion = discussionResult.opinion,
                    )
            }
        }

        fun getDraftDiscussionCount() {
            viewModelScope.launch {
                val count = discussionRepository.getDraftDiscussionCount()
                _uiState.value = _uiState.value?.copy(draftDiscussionCount = count)
            }
        }

        fun isPossibleToCreate() {
            if (uiState.value?.isPosting ?: false) {
                return
            }
            _uiState.value = _uiState.value?.copy(isPosting = true)
            val error = _uiState.value?.validate()

            if (error != null) {
                _uiEvent.setValue(CreateDiscussionUiEvent.ShowToast(error))
                _uiState.value = _uiState.value?.copy(isPosting = false)
                return
            }
            when (mode) {
                is SerializationCreateDiscussionRoomMode.Create -> createDiscussionRoom()
                is SerializationCreateDiscussionRoomMode.Draft -> createDiscussionRoom()
                is SerializationCreateDiscussionRoomMode.Edit -> editDiscussionRoom()
            }
        }

        fun checkIsPossibleToSave() {
            viewModelScope.launch {
                discussionRepository.getDiscussions()
                _uiEvent.setValue(CreateDiscussionUiEvent.SaveDraft(true))
                return@launch
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
                    }
                }
            }
        }

        fun saveDraft() {
            val editBook = _uiState.value?.editBook
            val book =
                if (editBook == null) {
                    _uiState.value?.editBook ?: _uiState.value?.draftBook ?: _uiState.value?.book
                        ?: run {
                            _uiEvent.setValue(
                                CreateDiscussionUiEvent.ShowToast(
                                    ErrorCreateDiscussionType.BOOK_INFO_NOT_FOUND,
                                ),
                            )
                            return
                        }
                } else {
                    SearchedBook.Companion.SearchedBook(
                        isbn = editBook.id,
                        title = editBook.title,
                        author = editBook.author,
                        image = editBook.image,
                    )
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
            viewModelScope.launch {
                discussionRepository.saveDiscussionRoom(
                    book = book as SearchedBook,
                    title,
                    opinion,
                )
            }
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

        private fun createDiscussionRoom() {
            val book =
                _uiState.value?.book ?: _uiState.value?.draftBook ?: run {
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
                        _uiState.value?.copy(isPosting = false)
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
                            editBook = result.book,
                            title = result.discussionTitle,
                            opinion = result.discussionOpinion,
                        )
                }
            }
        }

        private fun editDiscussionRoom() {
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
            getDiscussionRoom(discussionRoomId, title, opinion)
        }

        private fun getDiscussionRoom(
            discussionRoomId: Long,
            title: String,
            opinion: String,
        ) {
            val discussionRoom = DiscussionRoom(discussionRoomId, title, opinion)
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
            const val EXTRA_MODE = "mode"
            const val MODE_NOT_EXIST = "mode가 존재하지 않습니다."
        }
    }
