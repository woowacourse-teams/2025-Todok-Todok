package com.example.todoktodok.presentation.view.note.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Book
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
    val uiState get() = _uiState

    private val _uiEvent: MutableSingleLiveData<NoteUiEvent> = MutableSingleLiveData()
    val uiEvent: SingleLiveData<NoteUiEvent> get() = _uiEvent

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

    private fun sendShowBooksEvent(books: List<Book>) {
        val parcelables = mapToParcelableBook(books)
        onUiEvent(NoteUiEvent.ShowOwnBooks(parcelables))
    }

    private fun mapToParcelableBook(books: List<Book>): List<SerializationBook> =
        books.map {
            SerializationBook(
                id = it.id,
                title = it.title,
                author = it.author,
                image = it.image,
            )
        }

    private fun onUiEvent(event: NoteUiEvent) {
        _uiEvent.setValue(event)
    }
}
