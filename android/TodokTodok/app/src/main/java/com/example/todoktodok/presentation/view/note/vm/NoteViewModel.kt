package com.example.todoktodok.presentation.view.note.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Books
import com.example.domain.repository.BookRepository
import com.example.domain.repository.NoteRepository
import com.example.todoktodok.presentation.core.event.MutableSingleLiveData
import com.example.todoktodok.presentation.core.event.SingleLiveData
import com.example.todoktodok.presentation.view.note.NoteState
import com.example.todoktodok.presentation.view.note.NoteUiEvent
import com.example.todoktodok.presentation.view.serialization.SerializationBook
import kotlinx.coroutines.launch

class NoteViewModel(
    private val defaultBookRepository: BookRepository,
    private val defaultNoteRepository: NoteRepository,
) : ViewModel() {
    private val _uiState = MutableLiveData(NoteState())
    val uiState: LiveData<NoteState> get() = _uiState

    private val _uiEvent: MutableSingleLiveData<NoteUiEvent> = MutableSingleLiveData()
    val uiEvent: SingleLiveData<NoteUiEvent> get() = _uiEvent

    fun saveNote() {
        val currentUiState = _uiState.value ?: return

        currentUiState.selectedBook?.let {
            requestSaveNote(it.id, currentUiState.snap, currentUiState.memo)
        } ?: run {
            onUiEvent(NoteUiEvent.NotHasSelectedBook)
            loadOrShowSavedBooks()
        }
    }

    private fun requestSaveNote(
        selectedBookId: Long,
        snap: String,
        memo: String,
    ) {
        viewModelScope.launch {
            defaultNoteRepository.saveNote(selectedBookId, snap, memo)
        }
    }

    fun updateSnap(snap: String) {
        _uiState.value = _uiState.value?.copy(snap = snap)
    }

    fun updateMemo(memo: String) {
        _uiState.value = _uiState.value?.copy(memo = memo)
    }

    fun loadOrShowSavedBooks() {
        val savedBooks = _uiState.value?.savedBooks

        savedBooks?.let { sendShowBooksEvent(savedBooks) } ?: run { loadBooks() }
    }

    private fun loadBooks() {
        viewModelScope.launch {
            val books = defaultBookRepository.getBooks()
            _uiState.value = _uiState.value?.copy(savedBooks = books)
            sendShowBooksEvent(books)
        }
    }

    private fun sendShowBooksEvent(books: Books) {
        val parcelables = mapToParcelableBook(books)
        onUiEvent(NoteUiEvent.ShowOwnBooks(parcelables))
    }

    private fun mapToParcelableBook(books: Books): List<SerializationBook> =
        books.items.map {
            SerializationBook(
                id = it.id,
                title = it.title,
                author = it.author,
                image = it.image,
            )
        }

    fun updateSelectedBook(selectedIndex: Int) {
        val selectedBook = _uiState.value?.modifySelectedBook(selectedIndex)
        _uiState.value = selectedBook
    }

    private fun onUiEvent(event: NoteUiEvent) {
        _uiEvent.setValue(event)
    }
}
